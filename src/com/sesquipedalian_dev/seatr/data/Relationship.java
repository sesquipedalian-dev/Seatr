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

import com.sesquipedalian_dev.seatr.data.enums.RelationshipType;

import java.util.Set;

/**
 * Relationship represents a relationship between two people that could have a positive or negative effect on
 * the score of their seating nearby.  We're also saying all relationships are only between 2 people; to get the effect
 * of e.g. a love triangle, just create the individual two-way links for each leg.  It also is possible for people
 * to see the relationship differently, so this is a one-way map.  E.g. stalker thinks that target and she are besties,
 * but target hates the stalker.
 */
public class Relationship {
    private String sourcePersonName;
    private String targetPersonName;
    private RelationshipType type;

    public Relationship(String sourcePersonName, String targetPersonName, RelationshipType type) {
        this.sourcePersonName = sourcePersonName;
        this.targetPersonName = targetPersonName;
        this.type = type;
    }

    public int getScore() {
        return type.getScore();
    }

    public int getScore(Person source, Person target) {
        if((source.getName() == sourcePersonName) &&
           (target.getName() == targetPersonName))
        {
            return getScore();
        } else {
            return 0;
        }
    }

    public boolean involvesPerson(Person p) {
        return (sourcePersonName.compareTo(p.getName()) == 0) ||
               (targetPersonName.compareTo(p.getName()) == 0);
    }

    public String getSourcePersonName() {
        return sourcePersonName;
    }

    public String getTargetPersonName() {
        return targetPersonName;
    }

    public RelationshipType getType() {
        return type;
    }
}
