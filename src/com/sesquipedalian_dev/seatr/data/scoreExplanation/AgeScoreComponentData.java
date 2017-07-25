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
import com.sesquipedalian_dev.seatr.data.Seat;
import com.sesquipedalian_dev.util.DemographicAgeBrackets;

/**
 * AgeScoreComponentData
 */
public class AgeScoreComponentData implements ScoreComponentData {
    private Person p1;
    private Person p2;

    public AgeScoreComponentData(Person p1, Person p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public String renderString(Seat s, int score) {
        // score age similarities
        // ok, we want max effect to be -20 for the biggest possible age gap,
        // since that's about double an incompatible Tag
        int primaryAge = p1.getAgeInYears();
        int otherAge = p2.getAgeInYears();
        int primaryBracket = DemographicAgeBrackets.getDemographicAgeBracket(primaryAge);
        int otherBracket = DemographicAgeBrackets.getDemographicAgeBracket(otherAge);
        int bracketGap = Math.abs(primaryBracket - otherBracket);

        StringBuffer sb = new StringBuffer();
        sb.append(p1.getName());
        sb.append(" is aged {");
        sb.append(primaryAge);
        sb.append("} years, falling into age bracket {");
        sb.append(primaryBracket);
        sb.append("}.  ");
        sb.append(p2.getName());
        sb.append(" is aged {");
        sb.append(otherAge);
        sb.append("}, falling into age bracket {");
        sb.append(otherBracket);
        sb.append("}.  This gives them a bracket difference of {");
        sb.append(bracketGap);
        sb.append("}, for a score of {");
        sb.append(score);
        sb.append("}");

        return sb.toString();
    }
}
