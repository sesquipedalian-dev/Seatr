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
import com.sesquipedalian_dev.seatr.data.enums.TableShape;

import java.util.*;

/**
 * HardCodedInput
 */
public class HardCodedInput implements SeatrInput {
    public HardCodedInput() {
    }

    @Override
    public Map<String, Tag> loadTags() {
        Map<String, Tag> allTags = new HashMap<String, Tag>();

        allTags.put("nerd", new Tag("nerd", new HashSet<String>(Arrays.asList("geek", "vidyaGames")), new HashSet<String>(Arrays.asList("sports")), false));
        allTags.put("geek", new Tag("geek", new HashSet<String>(Arrays.asList("nerd")), new HashSet<String>(Arrays.asList("sports")), false));
        allTags.put("vidyaGames", new Tag("vidyaGames", new HashSet<String>(Arrays.asList("nerd")), new HashSet<String>(Arrays.asList("sports")), false));
        allTags.put("sports", new Tag("sports", new HashSet<String>(Arrays.asList()), new HashSet<String>(Arrays.asList("nerd", "geek", "vidyaGames")), false));

        return allTags;
    }

    @Override
    public GuestList loadGuests(Map<String, Tag> allTags, List<Table> emptyTables) {
        Set<GuestList.GuestGroup> guests = new HashSet<>();

        List<Person> guests1 = new ArrayList<>();
        guests1.add(new Person(new HashSet<>(Arrays.asList(allTags.get("nerd"))), "Bob", 23));
        guests1.add(new Person(new HashSet<>(Arrays.asList(allTags.get("sports"))), "Fred", 26));
        guests.add(new GuestList.GuestGroup(guests1));

        List<Person> guests2 = new ArrayList<>();
        guests2.add(new Person(new HashSet<>(Arrays.asList(allTags.get("nerd"))), "List", 23));
        guests2.add(new Person(new HashSet<>(Arrays.asList(allTags.get("sports"))), "Franklin", 26));
        guests2.add(new Person(new HashSet<>(Arrays.asList(allTags.get("vidyaGames"))), "Decklan", 5));
        guests2.add(new Person(new HashSet<>(Arrays.asList(allTags.get("geek"))), "Pretentia", 7));
        guests.add(new GuestList.GuestGroup(guests2));

        List<Person> guests3 = new ArrayList<>();
        guests3.add(new Person(new HashSet<>(Arrays.asList(allTags.get("sports"))), "Liger", 40));
        guests3.add(new Person(new HashSet<>(Arrays.asList(allTags.get("sports"))), "Leopard", 41));
        guests3.add(new Person(new HashSet<>(Arrays.asList(allTags.get("sports"))), "Cougar", 10));
        guests3.add(new Person(new HashSet<>(Arrays.asList(allTags.get("spots"))), "Mountain Lian", 12));
        guests.add(new GuestList.GuestGroup(guests3));

        List<Person> guests4 = new ArrayList<>();
        guests4.add(new Person(new HashSet<>(Arrays.asList(allTags.get("geek"))), "Bob1", 76));
        guests4.add(new Person(new HashSet<>(Arrays.asList(allTags.get("nerd"))), "Fred1", 21));
        guests.add(new GuestList.GuestGroup(guests4));

        List<Person> guests5 = new ArrayList<>();
        guests5.add(new Person(new HashSet<>(Arrays.asList(allTags.get("vidyaGames"))), "Bob2", 5));
        guests5.add(new Person(new HashSet<>(Arrays.asList(allTags.get("vidyaGames"))), "Fred2", 7));
        guests.add(new GuestList.GuestGroup(guests5));

        List<Person> guests6 = new ArrayList<>();
        guests6.add(new Person(new HashSet<>(Arrays.asList(allTags.get("sports"))), "Bob3", 29));
        guests6.add(new Person(new HashSet<>(Arrays.asList(allTags.get("sports"))), "Fred3", 32));
        guests.add(new GuestList.GuestGroup(guests6));

        return new GuestList(guests);
    }

    @Override
    public List<Table> loadEmptyTables() {
        List<Table> tables = new ArrayList<>();
        tables.add(new Table(TableShape.CIRCLE, 6));
        tables.add(new Table(TableShape.RECTANGLE, 10));
        tables.forEach(Table::initialize);
        return tables;
    }

    @Override
    public Set<Relationship> loadRelationships() {
        Set<Relationship> retVal = new HashSet<>();
        return retVal;
    }
}
