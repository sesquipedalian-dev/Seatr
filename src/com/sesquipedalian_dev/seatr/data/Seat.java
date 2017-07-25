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

import com.sesquipedalian_dev.seatr.data.enums.SeatRelationType;
import com.sesquipedalian_dev.seatr.data.enums.TagCompatibility;
import com.sesquipedalian_dev.seatr.data.scoreExplanation.AgeScoreComponentData;
import com.sesquipedalian_dev.seatr.data.scoreExplanation.RelationshipScoreComponentData;
import com.sesquipedalian_dev.seatr.data.scoreExplanation.ScoreComponent;
import com.sesquipedalian_dev.seatr.data.scoreExplanation.TagScoreComponentData;
import com.sesquipedalian_dev.util.DemographicAgeBrackets;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Seat - an individual seat at a table (that we want to fill for some event)
 */
public class Seat {
    public static class SeatRelation {
        Seat otherSeat;
        SeatRelationType relationType; // possibly left and right are higher 'weight' than 'across'

        public SeatRelation(Seat otherSeat) {
            this(otherSeat, SeatRelationType.NEXT);
        }
        public SeatRelation(Seat otherSeat, SeatRelationType relationType) {
            this.otherSeat = otherSeat;
            this.relationType = relationType;
        }

        public Seat getOtherSeat() {
            return otherSeat;
        }

        int score(Person primaryPerson, Set<Relationship> relationships) {
            int runningTotal = 0;
            Person otherPerson = otherSeat.getSeatedPerson();

            // score tags
            for(Tag primaryTag: primaryPerson.getTags()) {
                for(Tag otherTag: otherPerson.getTags()) {
                    TagCompatibility compatibility = primaryTag.getCompatibilityWith(otherTag);
                    runningTotal += Math.round(compatibility.getScore() * relationType.getWeight());
                }
            }

            // score relationships
            for(Relationship r: relationships) {
                if(r.involvesPerson(primaryPerson) && r.involvesPerson(otherPerson)) {
                    runningTotal += r.getScore();
                }
            }

            // score age similarities
            // ok, we want max effect to be -20 for the biggest possible age gap,
            // since that's about double an incompatible Tag
            int primaryAge = primaryPerson.getAgeInYears();
            int otherAge = otherPerson.getAgeInYears();
            int primaryBracket = DemographicAgeBrackets.getDemographicAgeBracket(primaryAge);
            int otherBracket = DemographicAgeBrackets.getDemographicAgeBracket(otherAge);
            int bracketGap = Math.abs(primaryBracket - otherBracket);
            runningTotal -= Math.round(bracketGap * ((float) (20 / 7)));

            return runningTotal;
        }

        public void appendScoreComponents(Person primaryPerson, Set<Relationship> relationships, List<ScoreComponent> runningTotal) {
            Person otherPerson = otherSeat.getSeatedPerson();

            // score tags
            for(Tag primaryTag: primaryPerson.getTags()) {
                for(Tag otherTag: otherPerson.getTags()) {
                    TagCompatibility compatibility = primaryTag.getCompatibilityWith(otherTag);
                    int score = (int) Math.round(compatibility.getScore() * relationType.getWeight());
                    runningTotal.add(new ScoreComponent(score,
                            new TagScoreComponentData(primaryTag, otherTag, otherPerson.getName()))
                    );
                }
            }

            // score relationships
            for(Relationship r: relationships) {
                if(r.involvesPerson(primaryPerson) && r.involvesPerson(otherPerson)) {
                    int score = r.getScore();
                    runningTotal.add(new ScoreComponent(score, new RelationshipScoreComponentData(r)));
                }
            }

            // score age similarities
            // ok, we want max effect to be -20 for the biggest possible age gap,
            // since that's about double an incompatible Tag
            int primaryAge = primaryPerson.getAgeInYears();
            int otherAge = otherPerson.getAgeInYears();
            int primaryBracket = DemographicAgeBrackets.getDemographicAgeBracket(primaryAge);
            int otherBracket = DemographicAgeBrackets.getDemographicAgeBracket(otherAge);
            int bracketGap = Math.abs(primaryBracket - otherBracket);
            int ageScore = -Math.round(bracketGap * ((float) (20 / 7)));
            runningTotal.add(new ScoreComponent(ageScore, new AgeScoreComponentData(primaryPerson, otherPerson)));
        }

        public SeatRelationType getRelationType() {
            return relationType;
        }
    }

    public Person getSeatedPerson() {
        return seatedPerson;
    }

    private List<SeatRelation> seatRelations;
    private Person seatedPerson;
    private boolean pinned;

    public Seat() {
        this(null, null, false);
    }

    public Seat(List<SeatRelation> seatRelations) {
        this(seatRelations, null, false);
    }

    public Seat(List<SeatRelation> seatRelations, Person seatedPerson) {
        this(seatRelations, seatedPerson, false);
    }

    public Seat(List<SeatRelation> seatRelations, Person seatedPerson, boolean pinned) {
        this.seatedPerson = seatedPerson;
        this.seatRelations = seatRelations;
        this.pinned = pinned;
    }

    public int score(Set<Relationship> relationships) {
        int runningTotal = 0;
        for(SeatRelation r: seatRelations) {
            runningTotal += r.score(seatedPerson, relationships);
        }
        return runningTotal;
    }

    public void setSeatRelations(List<SeatRelation> seatRelations) {
        this.seatRelations = seatRelations;
    }

    public List<SeatRelation> getSeatRelations() {
        return seatRelations;
    }

    public void seat(Person person) {
        this.seatedPerson = person;
    }

    public Seat copy() {
        // Table is gonna have to be responsible for this copy because it gets the reference to the other sets
        List<SeatRelation> relations = new ArrayList<>();

        return new Seat(relations, seatedPerson, pinned);
    }
}
