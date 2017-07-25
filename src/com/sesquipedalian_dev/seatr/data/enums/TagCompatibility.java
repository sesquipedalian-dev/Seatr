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
package com.sesquipedalian_dev.seatr.data.enums;

import com.sesquipedalian_dev.seatr.data.ScoreEnum;

/**
 * TagCompatibility enum represents the compatibility between tags
 */
public enum TagCompatibility implements ScoreEnum {
    IDENTITY(20),
    POSITIVE(10),
    NEGATIVE(-10),
    NEUTRAL(0);

    private final int score;
    TagCompatibility(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}
