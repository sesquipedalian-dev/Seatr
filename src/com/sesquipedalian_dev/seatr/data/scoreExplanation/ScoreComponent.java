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

/**
 * ScoreExplanation - holds details about how a score was calculated for a seat
 */
public class ScoreComponent {
    private int score;
    private ScoreComponentData data;

    public ScoreComponent(int score, ScoreComponentData data) {
        this.score = score;
        this.data = data;
    }

    /**
     *
     * @return the score provided by this piece of the score
     */
    public int getScore(){
        return score;
    }

    /**
     *
     * @return additional supporting data for the score - this can be used to render the explanation
     */
    public ScoreComponentData getData() {
        return data;
    }
}





