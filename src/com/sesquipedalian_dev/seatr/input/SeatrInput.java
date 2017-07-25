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
package com.sesquipedalian_dev.seatr.input;

import com.sesquipedalian_dev.seatr.data.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * SeatrInput - interface for retrieving stored guest list / empty tables information
 */

public interface SeatrInput {
    Map<String, Tag> loadTags();
    GuestList loadGuests(Map<String, Tag> tags, List<Table> emptyTables);
    List<Table> loadEmptyTables();
    Set<Relationship> loadRelationships();
}
