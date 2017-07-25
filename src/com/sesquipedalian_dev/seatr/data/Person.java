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
import java.util.Set;

/**
 * Person represents a guest that could be invited to an event.  People have various properties
 * that we use to seat them near people or parts of the event that will be benificent for them.
 *
 */
public class Person {
    private Set<Tag> tags;
    private String name; // TODO first / last names? meh
    private int ageInYears; // TODO unsure if this will be a useful indicator for matching; maybe make it optional for the end user?

    public Set<Tag> getTags() {
        return tags;
    }

    public String getName() {
        return name;
    }

    public int getAgeInYears() {
        return ageInYears;
    }

    public Person(String name) {
        this(new HashSet<Tag>(), name, 0);
    }

    public Person(String name, int ageInYears) {
        this(new HashSet<Tag>(), name, ageInYears);
    }

    public Person(Set<Tag> tags, String name, int ageInYears) {
        this.tags = tags;
        this.name = name;
        this.ageInYears = ageInYears;
    }

}
