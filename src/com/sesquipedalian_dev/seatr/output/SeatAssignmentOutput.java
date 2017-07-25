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
package com.sesquipedalian_dev.seatr.output;

import com.sesquipedalian_dev.seatr.data.Person;
import com.sesquipedalian_dev.seatr.data.Relationship;
import com.sesquipedalian_dev.seatr.data.Seat;
import com.sesquipedalian_dev.seatr.data.Table;
import com.sesquipedalian_dev.seatr.data.scoreExplanation.ScoreComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * SeatAssignmentOutput
 */
public class SeatAssignmentOutput {
    public void printSeatAssignment(List<Table> assignments, Set<Relationship> relationships) {
        int tableNum = 1;
        int runningTotalAll = 0;
        for(Table t: assignments) {
            int runningTotalTable = 0;
            System.out.println("Table " + tableNum + " (" + t.getShape() + "/" + t.getSeatCount() + "):");

            int seatNum = 1;
            for(Seat s: t.getSeats()) {
                Person seatedPerson = s.getSeatedPerson();
                int seatScore = s.score(relationships);

                if(seatedPerson != null) {
                    System.out.println("\tSeat " + seatNum + " (" + seatScore + "): " + s.getSeatedPerson().getName());
                    System.out.println("\t\tScore explanation:");
                    for(Seat.SeatRelation r: s.getSeatRelations()) {
                        System.out.println("\t\t\tSeated " + r.getRelationType() + " " + r.getOtherSeat().getSeatedPerson().getName() + ":");
                        List<ScoreComponent> explanations = new ArrayList<>();
                        r.appendScoreComponents(seatedPerson, relationships, explanations);
                        for(ScoreComponent sc: explanations) {
                            int score = sc.getScore();
                            if(score != 0) {
                                System.out.println("\t\t\t\t(" + score + "): " + sc.getData().renderString(s, score));
                            }
                        }
                    }
                } else {
                    System.out.println("\tSeat " + seatNum + ": EMPTY");
                }

                seatNum++;
                runningTotalTable += seatScore;
            }

            tableNum++;
            runningTotalAll += runningTotalTable;
            System.out.println("\n\tTable Score: (" + runningTotalTable + ")");
        }

        System.out.println("\n\nTOTAL SCORE: (" + runningTotalAll + ")");
    }
}
