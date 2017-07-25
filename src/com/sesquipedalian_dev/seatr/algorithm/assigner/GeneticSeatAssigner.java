/**
 * Copyright 2017 sesquipedalian.dev@gmail.com

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sesquipedalian_dev.seatr.algorithm.assigner;

import com.sesquipedalian_dev.seatr.data.*;
import com.sesquipedalian_dev.seatr.output.SeatAssignmentOutput;

import java.util.*;
import java.util.stream.Collectors;

/**
 * GeneticSeatAssigner optimize seat assignments using the genetic algorithm.  See comments in assign method for outline.
 * create initial population
 * loop:
 *   check termination conditions
 *     generations > max?
 *     any population member has threshold score?
 *     break if true
 *   select new population
 *     highest x% of pop carries over to next generation
 *     rest of the population has some probability of:
 *       crossover
 *       mutation
 * end loop
 * return highest scored option as result (store x highest for possible reference)
 */
public class GeneticSeatAssigner implements SeatAssigner {
    private static final int POPULATION_SIZE = 10;
    private static final int GENERATIONS = 100;
    private static final int THRESHOLD_SCORE_PER_PERSON = 60;
    private static final double ELITIST_PERCENT = .4;
    private static final double MUTATION_PERCENT = .1;
    private static final int MAX_MUTATION_ITERATIONS = 5; // max attempts we'll make at finding a random set of groups that can swap
    private static final double CROSSOVER_PERCENT = .25;
    private static final int MAX_CROSSOVER_POINTS = 2;


    class AssignmentWithScore implements Comparable<AssignmentWithScore> {
        List<Table> assignments;
        int score;
        AssignmentWithScore(List<Table> assignments, int score) {
            this.assignments = assignments;
            this.score = score;
        }

        // we want reversed order, so best table is at head of queue
        @Override
        public int compareTo(AssignmentWithScore o) {
            return o.score - score;
        }
    }

    private PriorityQueue<AssignmentWithScore> population;

    public GeneticSeatAssigner() {
        this.population = new PriorityQueue<>();
    }

    @Override
    public List<Table> assign(List<Table> tableDefinitions, GuestList guestList, Set<Relationship> relationships) {
        // create initial population
        SeatAssigner randomAssigner = new RandomSeatAssigner();
        SeatAssignmentOutput output = new SeatAssignmentOutput();
        for(int i = 0; i < POPULATION_SIZE; ++i) {
            List<Table> clonedTables = null;
            int iterations = 0;
            while((clonedTables == null) && (iterations < MAX_MUTATION_ITERATIONS)) {
                try {
                    clonedTables = randomAssigner.assign(tableDefinitions, guestList, relationships);
                } catch(IllegalArgumentException e) {
                    // try again
                }
            }
            // DEBUGGING
            //output.printSeatAssignment(clonedTables, relationships);
            // DEBUGGING

            AssignmentWithScore assignment = new AssignmentWithScore(clonedTables,
                clonedTables.stream().collect(Collectors.summingInt(t -> t.score(relationships)))
            );
            population.add(assignment);
        }

        int generations = 0;
        int guestCount = guestList.getGuests().stream().collect(Collectors.summingInt(gg -> gg.getPeople().size()));
        int scoreThreshold = guestCount * THRESHOLD_SCORE_PER_PERSON;
        int bestXToTake = (int) Math.floor(POPULATION_SIZE * ELITIST_PERCENT);
        long lastIteration = System.currentTimeMillis();
        long startTime = lastIteration;
        System.out.println("GA inputs {" + generations + "}{" + guestCount + "}{" + scoreThreshold + "}{" + bestXToTake + "}");

        // loop:
        while(true) {
            long thisIteration = System.currentTimeMillis();
            System.out.println("Score after generation: {" + (thisIteration - lastIteration) + " ms}{" + generations + "}/{" + GENERATIONS + "}: {" + population.peek().score + "}/{" + scoreThreshold + "}");
            lastIteration = thisIteration;

            // generations > max?
            boolean maxGenerations = generations >= GENERATIONS;

            // any population member has threshold score?
            boolean exceededScore = population.peek().score >= scoreThreshold;

            // check termination conditions
            if(maxGenerations || exceededScore) {
                // break out of loop if true
                break;
            }

            // select new population
            Iterator<AssignmentWithScore> oldPopulationIter = population.iterator();
            PriorityQueue<AssignmentWithScore> newPopulation = new PriorityQueue<>();

            // highest x% of pop carries over to next generation
            int i = 0;
            while(i < bestXToTake && oldPopulationIter.hasNext()) {
                newPopulation.add(oldPopulationIter.next());
                ++i;
            }

            // for the rest of the population, crossover and mutate them
            while(oldPopulationIter.hasNext()) {
                // crossover
                // collect a pair of individuals
                AssignmentWithScore p1 = oldPopulationIter.next();
                if (oldPopulationIter.hasNext()) {
                    AssignmentWithScore p2 = oldPopulationIter.next();

                    // crossover the pair
                    List<AssignmentWithScore> crossedOverAssignments = crossover(p1, p2, guestList, relationships);

                    // mutation
                    for(AssignmentWithScore a: crossedOverAssignments) {
                         AssignmentWithScore mutatedAssignment = mutate(a, guestList, relationships);
                         newPopulation.add(mutatedAssignment);
                     }
                } else {
                    // if not enough for a pair, just mutate the remaining individual
                    newPopulation.add(mutate(p1, guestList, relationships));
                }
            }

            population = newPopulation;

            // end loop
            generations++;
        }

        long finishTime = System.currentTimeMillis();
        System.out.println("GA complete.  Total time: {" + (finishTime - startTime) + " ms}, generations {" + generations + "}");

        // return highest scored option as result (store x highest for possible reference)
        return population.peek().assignments;
    }

    public List<AssignmentWithScore> crossover(AssignmentWithScore p1, AssignmentWithScore p2, GuestList guestList, Set<Relationship> relationships) {
        List<AssignmentWithScore> retVal = new ArrayList<>();
        int crossoversPerformed = 0;
        boolean swapTables = false;

        List<Table> retVal1 = new ArrayList<>();
        List<Table> retVal2 = new ArrayList<>();

        // (assume that tables correspond in both arrangements; that is, table 1 is the same size and shape in both
        //  assignments)
        // scan through tables...
        for(Iterator<Table> p1TableIter = p1.assignments.iterator(), p2TableIter = p2.assignments.iterator();
            p1TableIter.hasNext() && p2TableIter.hasNext();
        ) {
            Table p1Table = p1TableIter.next();
            Table p2Table = p2TableIter.next();

            GuestList.GuestGroup currentGroup1 = null;
            GuestList.GuestGroup currentGroup2 = null;

            Table newP1Table = new Table(p1Table.getShape(), p1Table.getSeatCount());
            newP1Table.initialize();
            Table newP2Table = new Table(p2Table.getShape(), p2Table.getSeatCount());
            newP2Table.initialize();

            // ... and scan through seats at tables
            for(int i = 0; i < newP1Table.getSeatCount(); ++i) {
                Seat p1Seat = p1Table.getSeats().get(i);
                Seat p2Seat = p2Table.getSeats().get(i);

                // are we still processing a guest group? in that case, continue with what we were doing
                // (only check crossover at guest group breaks)
                boolean currentlyProcessingGroup =
                    ((currentGroup1 != null) && currentGroup1.getPeople().contains(p1Seat.getSeatedPerson())) ||
                    ((currentGroup2 != null) && currentGroup2.getPeople().contains(p2Seat.getSeatedPerson()));


                currentGroup1 = findGroup(p1Seat.getSeatedPerson(), guestList);
                currentGroup2 = findGroup(p2Seat.getSeatedPerson(), guestList);

                // can only swap same-sized groups
                boolean groupsSameSize = currentGroup1.getPeople().size() == currentGroup2.getPeople().size();

                // for each table passed, check the CROSSOVER_PERCENT. if true, swap which table is used as child 1 and 2
                boolean hitCrossoverPercent = Math.random() < CROSSOVER_PERCENT;
                boolean maxCrossoversPerformed = crossoversPerformed >= MAX_CROSSOVER_POINTS;

                if(groupsSameSize && !currentlyProcessingGroup && !maxCrossoversPerformed && hitCrossoverPercent) {
                    swapTables = !swapTables;
                    crossoversPerformed++;
                }

                // if tables are swapped, read from parent 2 into child 1 and parent 1 into child 2,
                // else read from parent 1 into child 1 and parent 2 into child 2
                if(swapTables) {
                    newP1Table.getSeats().get(i).seat(p2Seat.getSeatedPerson());
                    newP2Table.getSeats().get(i).seat(p1Seat.getSeatedPerson());
                } else {
                    newP1Table.getSeats().get(i).seat(p1Seat.getSeatedPerson());
                    newP2Table.getSeats().get(i).seat(p2Seat.getSeatedPerson());
                }
            }

            retVal1.add(newP1Table);
            retVal2.add(newP2Table);
        }

        try {
            // cleanup pass - for each assignment set, if any group is seated multiple time,
            // the assignment is missing some guest groups.  Go through and clean up by
            // swapping in unused groups
            fixCrossoverSwaps(retVal1, guestList);
            fixCrossoverSwaps(retVal2, guestList);

            // save retVals
            retVal.add(new AssignmentWithScore(retVal1, retVal1.stream().collect(Collectors.summingInt(t -> t.score(relationships)))));
            retVal.add(new AssignmentWithScore(retVal2, retVal2.stream().collect(Collectors.summingInt(t -> t.score(relationships)))));

            // DEBUGGING
//            if(doTablesCorrespond(retVal1, p1.assignments)) {
//                System.out.println("parent 1 tables matched child after crossover!?");
//            } else {
//                System.out.println("parent 1 tables didn't match child after crossover!?");
//            }
//
//            if(doTablesCorrespond(retVal2, p2.assignments)) {
//                System.out.println("parent 2 tables matched child after crossover!?");
//            } else {
//                System.out.println("parent 2 tables didn't match child after crossover!?");
//            }
        } catch (IllegalArgumentException e) {
            System.out.println("Caught illegal swap fix up in crossover; using original parents!");
            retVal.add(p1);
            retVal.add(p2);
        }

        return retVal;
    }

    public boolean doTablesCorrespond(List<Table> a1, List<Table> a2) {
        Iterator<Table> a1Iter = a1.iterator();
        Iterator<Table> a2Iter = a2.iterator();
        for(;a1Iter.hasNext();) {
            if(!a2Iter.hasNext()) {
                return false;
            }
            Table a1Table = a1Iter.next();
            Table a2Table = a2Iter.next();

            Iterator<Seat> a1SeatIter = a1Table.getSeats().iterator();
            Iterator<Seat> a2SeatIter = a2Table.getSeats().iterator();
            for(;a1SeatIter.hasNext();) {
                if(!a2SeatIter.hasNext()) {
                    return false;
                }

                if(a1SeatIter.next().getSeatedPerson().getName() != a2SeatIter.next().getSeatedPerson().getName()) {
                    return false;
                }
            }

            if(a2SeatIter.hasNext()) {
                return false;
            }
        }
        if(a2Iter.hasNext()) {
            return false;
        }

        return true;
    }

    public void fixCrossoverSwaps(List<Table> tablesToFix, GuestList guestList) {
        // figure out which groups are used multiple times
        Map<GuestList.GuestGroup, Double> guestListUseCounts = new HashMap<>();
        for(Table t: tablesToFix) {
            for(Seat s: t.getSeats()) {
                GuestList.GuestGroup gg = findGroup(s.getSeatedPerson(), guestList);
                double currentCount = guestListUseCounts.getOrDefault(gg, 0.0);
                guestListUseCounts.put(gg, currentCount + 1.0 / gg.getPeople().size());
            }
        }

        // find the unused groups
        List<GuestList.GuestGroup> unusedGroups = guestList.getGuests().stream().filter(gg -> guestListUseCounts.get(gg) == null).collect(Collectors.toList());
        Collections.shuffle(unusedGroups);

        if(guestListUseCounts.size() + unusedGroups.size() != guestList.getGuests().size()) {
            System.out.println("all guests not accounted for!");
        }

        Iterator<Person> currentFixUpGroup = null;
        // fix up part
        for(Table t: tablesToFix) {
            for (Seat s : t.getSeats()) {
                if((currentFixUpGroup != null) && (currentFixUpGroup.hasNext())) {
                    s.seat(currentFixUpGroup.next());
                } else {
                    GuestList.GuestGroup gg = findGroup(s.getSeatedPerson(), guestList);
                    double currentCount = guestListUseCounts.getOrDefault(gg, 0.0);
                    if (currentCount > 1.0) {
                        Optional<GuestList.GuestGroup> swappedGroup = unusedGroups.stream().filter(gc -> gc.getPeople().size() == gg.getPeople().size()).findFirst();
                        if(swappedGroup.isPresent()) {
                            currentFixUpGroup = swappedGroup.get().getPeople().iterator();
                            unusedGroups.remove(swappedGroup.get());
                            s.seat(currentFixUpGroup.next());
                            guestListUseCounts.put(gg, currentCount - 1);
                        } else {
                            throw new IllegalArgumentException("Attempt to fix crossover swaps, but can't find a group of correct size");
                        }
                    }
                }
            }
        }
    }

    public GuestList.GuestGroup findGroup(Person p, GuestList guestList) {
        for(GuestList.GuestGroup g: guestList.getGuests()) {
            if(g.getPeople().contains(p)) {
                return g;
            }
        }
        return null;
    }

    public AssignmentWithScore mutate(AssignmentWithScore parent, GuestList guestList, Set<Relationship> relationships) {
        List<Table> oldTables = parent.assignments;

        List<Table> retVal = parent.assignments.stream().map(t -> t.copy()).collect(Collectors.toList());

        int totalSeats = oldTables.stream().collect(Collectors.summingInt(t -> t.getSeatCount()));
        int mutations = (int) Math.floor(totalSeats * MUTATION_PERCENT);

        List<SwapGroupsStruct> swapsToDo = new ArrayList<>();
        for(int i = 0; i < mutations; ++i) {
            int midPoint = totalSeats / 2;
            int midPointAdd = totalSeats % 2 == 0 ? 0 : 1; // offset for non-even seat counts

            GuestList.GuestGroup swap1Group = null;
            GuestList.GuestGroup swap2Group = null;
            boolean isDupe = false;

            int iterations = 0;
            do {
                int swap1 = (int) Math.floor(Math.random() * midPoint);
                int swap2 = (int) Math.floor(Math.random() * (midPoint + midPointAdd)) + midPoint;

                // determine which guest group the people at those seats are a part of
                Person swap1Person = personAtTableIndex(oldTables, swap1);
                swap1Group = guestList.getGuests().stream().filter(gg -> gg.getPeople().contains(swap1Person)).findFirst().get();

                Person swap2Person = personAtTableIndex(oldTables, swap2);
                swap2Group = guestList.getGuests().stream().filter(gg -> gg.getPeople().contains(swap2Person)).findFirst().get();

                if(swap1Group == swap2Group) {
                    // don't count a swap of the same group
                    //System.out.println("swap of same group!!!");
                    swap1Group = null;
                    swap2Group = null;
                    continue;
                }
                if(swap1Group.getPeople().size() != swap2Group.getPeople().size()) {
                    // can't swap missized groups - try again
                    swap1Group = null;
                    swap2Group = null;
                    continue;
                }

                isDupe = false;
                for(SwapGroupsStruct s: swapsToDo) {
                    isDupe = isDupe ||
                        (s.group1 == swap1Group) ||
                        (s.group2 == swap1Group) ||
                        (s.group1 == swap2Group) ||
                        (s.group2 == swap2Group);
                }
            } while((++iterations < MAX_MUTATION_ITERATIONS) && (swap1Group == null || swap2Group == null || isDupe));

            if(swap1Group != null && swap2Group != null) {
                swapsToDo.add(new SwapGroupsStruct(swap1Group, swap2Group));
            }
        }

        performSwaps(retVal, swapsToDo);

        return new AssignmentWithScore(retVal, retVal.stream().collect(Collectors.summingInt(t -> t.score(relationships))));
    }

    // swap all the seating groups provided in swapIndices in-place in the input tables
    private void performSwaps(List<Table> tables, List<SwapGroupsStruct> swapGroups) {
        // for each swap we have to do
        for(SwapGroupsStruct sgs: swapGroups) {
            List<Seat> group1Seats = new ArrayList<>();
            List<Seat> group2Seats = new ArrayList<>();

            // sweep through the table, and find the Seats associated with each swapping group
            for (Table t : tables) {
                for (Seat s : t.getSeats()) {
                    Person seatedPerson = s.getSeatedPerson();
                    if(sgs.group1.getPeople().contains(seatedPerson)) {
                        group1Seats.add(s);
                    } else if(sgs.group2.getPeople().contains(seatedPerson)) {
                        group2Seats.add(s);
                    }
                }
            }

            // zip group 1's seats with group 2's seats,
            // and go through and swap the person seated at each pair of seats
            for(Iterator<Seat> group1Iter = group1Seats.iterator(), group2Iter = group2Seats.iterator();
                group1Iter.hasNext() && group2Iter.hasNext()                                           ;
            ) {
                Seat seat1 = group1Iter.next();
                Seat seat2 = group2Iter.next();
                Person intermediate = seat1.getSeatedPerson();
                seat1.seat(seat2.getSeatedPerson());
                seat2.seat(intermediate);
            }
        }
    }

    // given a flat index into tables, find the person seated at that seat.
    // the first table's first seat is index 0, 2nd seat is 1... until the end of that table,
    // then the second table picks up where the index left off
    private Person personAtTableIndex(List<Table> tables, int index) {
        int i = 0;
        boolean found = false;
        for(Table t: tables) {
            for(Seat s: t.getSeats()) {
                if(i == index) {
                    return s.getSeatedPerson();
                }
                i++;
            }
        }
        return null;
    }

    // Tuple of guest list groups that need to be swapped
    class SwapGroupsStruct {
        GuestList.GuestGroup group1;
        GuestList.GuestGroup group2;

        SwapGroupsStruct(GuestList.GuestGroup g1, GuestList.GuestGroup g2) {
            group1 = g1;
            group2 = g2;
        }
    }
}
