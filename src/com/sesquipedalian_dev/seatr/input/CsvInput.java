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
import com.sesquipedalian_dev.seatr.data.enums.RelationshipType;
import com.sesquipedalian_dev.seatr.data.enums.TableShape;
import com.sesquipedalian_dev.util.CSVReader;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * CsvInput - load data from a csv.  The caller will provide a base file name, and we'll add on known suffixes
 * to know which files to load from the testData directory.
 */
public class CsvInput implements SeatrInput {
    private static final String peopleSuffix       = " - People.csv";
    private static final String relationshipSuffix = " - Relationships.csv";
    private static final String tagsSuffix         = " - Tags.csv";
    private static final String tablesSuffix       = " - Tables.csv";

    private static final String commaSeparatorRegex = "\\s*,\\s*";

    private String baseFilename;

    public CsvInput(String filename) {
        this.baseFilename = filename;
    }

    @Override
    public Map<String, Tag> loadTags() {
        String tagFilename = baseFilename + tagsSuffix;
        CSVReader reader = new CSVReader(tagFilename);

        Map<String, Tag> retVal = new HashMap<>();

        try {
            List<List<String>> tagInput = reader.parseFile();
            for(List<String> row: tagInput) {
                Set<String> compatibleTags = new HashSet<>();
                if(row.get(1).length() > 0) {
                    compatibleTags.addAll(Arrays.asList(row.get(1).split(commaSeparatorRegex)));
                }
                if(row.get(2).length() > 0) {
                    compatibleTags.addAll(Arrays.asList(row.get(2).split(commaSeparatorRegex)));
                }


                Set<String> incompatibleTags = new HashSet<>();
                if(row.get(3).length() > 0) {
                    incompatibleTags.addAll(Arrays.asList(row.get(3).split(commaSeparatorRegex)));
                }
                if(row.get(4).length() > 0) {
                    incompatibleTags.addAll(Arrays.asList(row.get(4).split(commaSeparatorRegex)));
                }

                Tag newTag = new Tag(
                    row.get(0),
                    compatibleTags,
                    incompatibleTags,
                    Boolean.parseBoolean(row.get(5))
                );

                retVal.put(newTag.getName(), newTag);
            }
        } catch(IOException e) {
            System.err.println("Couldn't parse file {" + tagFilename + "}");
        }

        return retVal;
    }

    @Override
    public GuestList loadGuests(Map<String, Tag> tags, List<Table> emptyTables) {
        String peopleFilename = baseFilename + peopleSuffix;
        CSVReader reader = new CSVReader(peopleFilename);

        Set<GuestList.GuestGroup> groups = new HashSet<>();

        try {
            List<List<String>> peopleInput = reader.parseFile();
            List<Person> currentGroup = null;

            for(List<String> row: peopleInput) {
                String name = row.get(0);
                String partyLeaderName = row.get(1);
                if((currentGroup == null) || (currentGroup.get(0).getName().compareTo(partyLeaderName) != 0)) {
                    if(currentGroup != null) {
                        groups.add(new GuestList.GuestGroup(currentGroup));
                    }
                    currentGroup = new ArrayList<>();
                }

                Set<Tag> t = new HashSet<>();
                if(row.get(3).length() > 0) {
                    String[] tagNames = row.get(3).split(commaSeparatorRegex);
                    t.addAll(Arrays.asList(tagNames).stream().map(s -> tags.get(s)).collect(Collectors.toSet()));
                }

                Person newPerson = new Person(
                    t,
                    name,
                    Integer.parseInt(row.get(2))
                );
                currentGroup.add(newPerson);

                String peggedTable = row.get(4);
                String peggedSeat = row.get(5);
                if((!peggedTable.isEmpty()) && (!peggedSeat.isEmpty())) {
                    int pt = Integer.parseInt(peggedTable) - 1;  // humans generally prefer 1-indexed
                    int ps = Integer.parseInt(peggedSeat) - 1;

                    emptyTables.get(pt).getSeats().get(ps).seat(newPerson);
                }
            }

            // do last group
            if(currentGroup != null) {
                groups.add(new GuestList.GuestGroup(currentGroup));
            }
        } catch(IOException e) {
            System.err.println("Couldn't parse file {" + peopleFilename + "}");
        }

        return new GuestList(groups);
    }

    @Override
    public List<Table> loadEmptyTables() {
        String tablesFilename = baseFilename + tablesSuffix;
        CSVReader reader = new CSVReader(tablesFilename);

        List<Table> retVal = new ArrayList<>();

        try {
            List<List<String>> tablesInput = reader.parseFile();
            for(List<String> row: tablesInput) {
                Table newTable = new Table(
                    TableShape.valueOf(row.get(1)),
                    Integer.parseInt(row.get(2))
                );

                retVal.add(newTable);
            }
        } catch(IOException e) {
            System.err.println("Couldn't parse file {" + tablesFilename + "}");
        } catch(IllegalArgumentException e) {
            System.err.println("Couldn't parse table shape");
        }

        return retVal;
    }

    @Override
    public Set<Relationship> loadRelationships() {
        Set<Relationship> retVal = new HashSet<>();
        String relationshipFilename = baseFilename + relationshipSuffix;
        CSVReader reader = new CSVReader(relationshipFilename);

        try {
            List<List<String>> relationshipInput = reader.parseFile();
            for(List<String> row: relationshipInput) {
                String person1Name = row.get(0);
                String person2Name = row.get(1);
                RelationshipType rt = RelationshipType.valueOf(row.get(2));

                retVal.add(new Relationship(person1Name, person2Name, rt));
            }
        } catch(IOException e) {
            System.err.println("Couldn't parse file {" + relationshipFilename + "}");
        }

        return retVal;
    }
}
