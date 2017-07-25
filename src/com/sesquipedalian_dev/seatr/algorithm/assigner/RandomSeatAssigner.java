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

import java.util.*;
import java.util.stream.Collectors;

/**
 * RandomSeatAssigner - randomly shuffle the seat assignments
 */
public class RandomSeatAssigner implements SeatAssigner {
    @Override
    public List<Table> assign(List<Table> tableDefinitions, GuestList guestList, Set<Relationship> relationships) {
        List<Table> retVal = tableDefinitions.stream()
                .map(t -> t.copy())
                .collect(Collectors.toList());


        Set<GuestList.GuestGroup> groups = guestList.getGuests();
        List<GuestList.GuestGroup> people = groups.stream().map(g -> g.copy()).collect(Collectors.toList());
        Collections.sort(people, new GroupComparator());

        // first pass: remove pegged seating assignments from consideration
        for(Table t: retVal) {
            List<Seat> seats = t.getSeats();

            for (Seat s : seats) {
                Person seatedPerson = s.getSeatedPerson();
                if (seatedPerson != null) {
                    // person already seated - remove them from our guest list to seat
                    List<Person> listToRmFrom = null;
                    Person personToRm = null;
                    GuestList.GuestGroup groupToRm = null;

                    for (GuestList.GuestGroup g : people) {
                        List<Person> guestGroupPeople = g.getPeople();
                        for (Person groupPerson : guestGroupPeople) {
                            if (groupPerson.getName().compareTo(seatedPerson.getName()) == 0) {
                                listToRmFrom = guestGroupPeople;
                                personToRm = groupPerson;

                                if (guestGroupPeople.size() <= 1) {
                                    groupToRm = g;
                                }

                                break;
                            }
                        }
                    }

                    if(listToRmFrom != null && personToRm != null) {
                        listToRmFrom.remove(personToRm);
                    }

                    if(groupToRm != null) {
                        people.remove(groupToRm);
                    }
                }
            }
        }

        // shuffle the tables; this will make sure different sized groups are tried in other tables than
        // just the first one that comes up
        Collections.shuffle(retVal);

        // second pass: fill remaining seats
        while(people.size() > 0) {
            GuestList.GuestGroup currentGroup = people.get(0);
            people.remove(currentGroup);

            boolean found = false;
            for(Table t: retVal) {
                if(t.seatGroup(currentGroup)) {
                    found = true;
                    break;
                }
            }
            if(!found) {
                throw new IllegalArgumentException("can't seat group: no remaining tables where they fit!");
            }
        }

        // unshuffle the table list
        List<Table> retVal2 = new ArrayList<>();
        for(Table t: tableDefinitions) {
            for(Table t2: retVal) {
                if(t.getShape() == t2.getShape() && t.getSeatCount() == t2.getSeatCount()) {
                    retVal2.add(t2);
                    retVal.remove(t2);
                    break;
                }
            }
        }

        return retVal2;
    }

    class GroupComparator implements Comparator<GuestList.GuestGroup> {
        @Override
        public int compare(GuestList.GuestGroup o1, GuestList.GuestGroup o2) {
            int s1 = o1.getPeople().size();
            int s2 = o2.getPeople().size();
            if(s1 == s2) {
                return ((int) Math.floor((Math.random() * 3))) - 1; // randomly permute within groups of equal size
            } else {
                return s2 - s1;
            }
        }
    }
}
