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

import com.sesquipedalian_dev.seatr.data.GuestList;
import com.sesquipedalian_dev.seatr.data.Relationship;
import com.sesquipedalian_dev.seatr.data.Table;

import java.util.List;
import java.util.Set;

/**
 * SeatAssigner - assigns people to seats based on a set of tables and a guest list
 * tableDefinitions are modified in-place - please to send in a copy
 */
@FunctionalInterface
public interface SeatAssigner {
    List<Table> assign(List<Table> tableDefinitions, GuestList guestList, Set<Relationship> relationships);
}
