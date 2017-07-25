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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * GuestList - list of people attending this event
 */
public class GuestList {
    public static class GuestGroup {
        List<Person> people;
        double weight;

        public List<Person> getPeople() {
            return people;
        }

        // TODO should implement Person interface since the group's relationship is basically the intersection
        // of everyone's picadillos? eh not really though

        public GuestGroup(List<Person> people) {
            this(people, 1.0);
        }

        public GuestGroup(List<Person> people, double weight) {
            this.people = people;
            this.weight = weight;
        }

        public GuestGroup copy() {
            List<Person> newList = new ArrayList<>(people);
            return new GuestGroup(newList, weight);
        }
    }

    private Set<GuestGroup> guests;

    public Set<GuestGroup> getGuests() {
        return guests;
    }

    public GuestList(Set<GuestGroup> guests) {
        this.guests = guests;
    }

    // TODO some methods relating to shuffling people around the groups
}
