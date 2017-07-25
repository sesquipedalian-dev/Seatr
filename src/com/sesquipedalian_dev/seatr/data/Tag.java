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

import com.sesquipedalian_dev.seatr.data.enums.TagCompatibility;

import java.util.HashSet;
import java.util.Set;

/**
 * Tag is a property that can be ascribed to a person in our database.  Tags can have positive or negative relationships
 * with other tags (e.g. nerds + board game = compatible, nerds + jocks = incompatible).  Tags can be user-created,
 * or come baked into the system.
 *
 */
public class Tag {
    private String name;
    private Set<String> compatibleTags;
    private Set<String> incompatibleTags;
    private boolean isUserCreated;

    public String getName() {
        return name;
    }

    public boolean getUserCreated() {
        return isUserCreated;
    }

    public TagCompatibility getCompatibilityWith(Tag other) {
        TagCompatibility retVal = TagCompatibility.NEUTRAL;

        if(other.getName() == name) {
            retVal = TagCompatibility.IDENTITY;
        }
        else if(compatibleTags.contains(other.name)) {
            retVal = TagCompatibility.POSITIVE;
        } else if (incompatibleTags.contains(other.name)) {
            retVal = TagCompatibility.NEGATIVE;
        }

        return retVal;
    }

    public Tag(String name) {
        this(name, new HashSet<String>(), new HashSet<String>(), true);
    }

    public Tag(String name, Set<String> compatibleTags, Set<String> incompatibleTags, boolean userCreated) {
        this.name = name;
        this.compatibleTags = compatibleTags;
        this.incompatibleTags = incompatibleTags;
        this.isUserCreated = userCreated;
    }
}
