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
package com.sesquipedalian_dev.seatr.data;

import com.sesquipedalian_dev.seatr.data.enums.TableShape;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Table - a grouping of Persons attending an event.  This is what we seek to optimize!
 */
public class Table {
    private List<Seat> seats;
    private TableShape shape;
    private int seatCount;
    private boolean initialized = false;

    public TableShape getShape() {
        return shape;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public List<Seat> getSeats() {
        if(!initialized) {
            initialize();
        }
        return seats;
    }

    // TODO we might do some more fancy things:
    // - physical position in a room
    // - proximity to particular features of the event (e.g. head table, speakers, bar)

    public Table(TableShape shape, int seatCount) {
        this(shape, seatCount, shape.generateSeats(seatCount));
    }

    public Table(TableShape shape, int seatCount, List<Seat> seats) {
        this.shape = shape;
        this.seatCount = seatCount;
        this.seats = seats;
    }

    public int score(Set<Relationship> relationships) {
        // TODO I'm not sure which version of this I like better
//        int retVal = 0;
//        for(Seat s: seats){
//            retVal += s.score();
//        }
//        return retVal;
        return seats.stream().map(s -> s.score(relationships)).reduce((s1, s2) -> s1 + s2).orElseGet(() -> 0);
    }

    public void initialize() {
        for(int i = 0; i < seats.size(); ++i) {
            seats.get(i).setSeatRelations(shape.getRelationsFor(i, seats));
        }
    }

    public Table copy() {
        if(!initialized) {
            initialize();
        }
        List<Seat> newSeatCol = seats.stream().map(s -> s.copy()).collect(Collectors.toList());

        List<List<Integer>> seatRelationshipsByIndex = new ArrayList<>();
        for(int i = 0; i < seats.size(); ++i) {
            List<Integer> relatedSeats = new ArrayList<>();
            Seat curSeat = seats.get(i);

            for(Seat.SeatRelation relation: curSeat.getSeatRelations()) {
                relatedSeats.add(seats.indexOf(relation.otherSeat));
            }
            seatRelationshipsByIndex.add(relatedSeats);
        }

        Iterator<List<Integer>> relatedSeatsIter = seatRelationshipsByIndex.iterator();

        for(int i = 0;i < seats.size() && relatedSeatsIter.hasNext(); ++i) {
            List<Seat.SeatRelation> relatedSeats = new ArrayList<>();
            Seat oldSeat = seats.get(i);
            Seat newSeat = newSeatCol.get(i);

            List<Integer> relatedSeatsList = relatedSeatsIter.next();
            for(int j =0; j < relatedSeatsList.size(); ++j) {
                Seat.SeatRelation origRelation = oldSeat.getSeatRelations().get(j);
                Seat otherSeat = newSeatCol.get(relatedSeatsList.get(j));

                Seat.SeatRelation newRelation = new Seat.SeatRelation(otherSeat, origRelation.relationType);
                relatedSeats.add(newRelation);
            }

            newSeat.setSeatRelations(relatedSeats);
        }

        return new Table(shape, seatCount, newSeatCol);
    }

    // attempt to seat the given group in the first available contiguous group of seats
    public boolean seatGroup(GuestList.GuestGroup group) {
        List<List<Seat>> possibleSeatGroups = new ArrayList<>();
        List<Seat> currentSeatGroup = new ArrayList<>();
        for(Seat s: seats) {
            if(s.getSeatedPerson() == null) {
                currentSeatGroup.add(s);
                if(currentSeatGroup.size() >= group.getPeople().size()) {
                    possibleSeatGroups.add(new ArrayList<>(currentSeatGroup));
                    currentSeatGroup.remove(0);
                }
            } else {
                currentSeatGroup.clear();
            }
        }

        if(possibleSeatGroups.size() < 1) {
            return false;
        }

        Collections.shuffle(possibleSeatGroups);
        currentSeatGroup = possibleSeatGroups.get(0);

        Iterator<Person> nextP = group.getPeople().iterator();
        for(Seat ss: currentSeatGroup) {
            ss.seat(nextP.next());
        }

        return true;
    }
}
