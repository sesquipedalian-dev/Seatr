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
package com.sesquipedalian_dev.seatr.data.scoreExplanation;

import com.sesquipedalian_dev.seatr.data.Person;
import com.sesquipedalian_dev.seatr.data.Relationship;
import com.sesquipedalian_dev.seatr.data.Seat;
import com.sesquipedalian_dev.seatr.data.Tag;

/**
 * TagScoreComponentData
 */
public class TagScoreComponentData implements ScoreComponentData {
    private Tag t1;
    private Tag t2;
    private String otherPersonName;

    public TagScoreComponentData(Tag t1, Tag t2, String otherPersonName) {
        this.t1 = t1;
        this.t2 = t2;
        this.otherPersonName = otherPersonName;
    }

    @Override
    public String renderString(Seat s, int score) {
        StringBuffer sb = new StringBuffer();

        Person primaryPerson = s.getSeatedPerson();

        sb.append(primaryPerson.getName());
        sb.append("'s tag {");
        sb.append(t1.getName());
        sb.append("} relates to ");
        sb.append(otherPersonName);
        sb.append("'s tag {");
        sb.append(t2.getName());
        sb.append("} with compatibility {");
        sb.append(t1.getCompatibilityWith(t2));
        sb.append("} for a score of {");
        sb.append(score);
        sb.append("}.");

        return sb.toString();
    }
}

