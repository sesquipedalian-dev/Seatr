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

/**
 * RelationshipScoreComponentData
 */

public class RelationshipScoreComponentData implements ScoreComponentData {
    private Relationship r;

    public RelationshipScoreComponentData(Relationship r) {
        this.r = r;
    }

    @Override
    public String renderString(Seat s, int score) {
        StringBuffer sb = new StringBuffer();
        Person primaryPerson = s.getSeatedPerson();
        String otherPersonName;
        if(r.getSourcePersonName() != primaryPerson.getName()) {
            otherPersonName = r.getTargetPersonName();
        } else {
            otherPersonName = r.getSourcePersonName();
        }

        sb.append(primaryPerson.getName());
        sb.append(" has relation ship of type {");
        sb.append(r.getType());
        sb.append("} with ");
        sb.append(otherPersonName);
        sb.append(" for score {");
        sb.append(score);
        sb.append("}");

        return sb.toString();
    }
}
