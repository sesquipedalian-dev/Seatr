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
 * TableSeatsGenerator - generates a set of Seats (and relationships) for a given size of this type of table
 */
@FunctionalInterface
public interface TableSeatsGenerator {
    default List<Seat> generateSeats(int count) {
        List<Seat> retVal = new ArrayList<>();
        for(int i = 0; i < count; ++i) {
            retVal.add(new Seat());
        }
        return retVal;
    }
    List<Seat.SeatRelation> getRelationsFor(int position, List<Seat> seats);
}
