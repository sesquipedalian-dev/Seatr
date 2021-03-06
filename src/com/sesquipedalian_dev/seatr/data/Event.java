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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Event - an event for which we are assigning seating
 */
public class Event {
    private GuestList guestList;
    private List<Table> tables;

    private Set<TableScore> assignments;

    private class TableScore {
        List<Table> assignments;
        int score;

        TableScore(List<Table> assignments, int score) {
            this.assignments = assignments;
            this.score = score;
        }
    }

    public Event(GuestList guestList, List<Table> tables) {
        this.guestList = guestList;
        this.tables = tables;
        this.assignments = new HashSet<TableScore>();
    }

    public int score() {
        // TODO
        return 0;
    }
}
