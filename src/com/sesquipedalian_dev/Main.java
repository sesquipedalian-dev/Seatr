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
package com.sesquipedalian_dev;

import com.sesquipedalian_dev.seatr.algorithm.assigner.GeneticSeatAssigner;
import com.sesquipedalian_dev.seatr.algorithm.assigner.RandomSeatAssigner;
import com.sesquipedalian_dev.seatr.data.*;
import com.sesquipedalian_dev.seatr.input.CsvInput;
import com.sesquipedalian_dev.seatr.input.HardCodedInput;
import com.sesquipedalian_dev.seatr.input.SeatrInput;
import com.sesquipedalian_dev.seatr.output.SeatAssignmentOutput;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

// args[0] - file path to input files - ' - People.csv' etc suffixes will be added to this
// args[1] - if 'random' then use random seat assignments instead of genetic algorithm
public class Main {
    public static void main(String[] args) {
        String inputPath = args[0];
        SeatrInput input;
        try {
            input = new CsvInput(inputPath);
        } catch(Exception e) {
            System.out.println("Couldn't load input path from args: " + inputPath + "; using hard-coded input instead");
            input = new HardCodedInput();
        }

        Map<String, Tag> tags = input.loadTags();
        List<Table> tables = input.loadEmptyTables();
        GuestList guestList = input.loadGuests(tags, tables);
        Set<Relationship> relationships = input.loadRelationships();

        List<Table> assignedTables;
        String algorithm = args[1];
        if(algorithm.equals("random")) {
            assignedTables = new RandomSeatAssigner().assign(tables, guestList, relationships);
        } else {
            assignedTables = new GeneticSeatAssigner().assign(tables, guestList, relationships);
        }

        System.out.println("******FINAL******\n\n");
        new SeatAssignmentOutput().printSeatAssignment(assignedTables, relationships);
    }
}
