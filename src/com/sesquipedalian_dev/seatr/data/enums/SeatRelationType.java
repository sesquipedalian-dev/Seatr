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

/**
 * SeatRelationType
 */
public enum SeatRelationType {
    ACROSS(.5, "across from"),
    NEXT(1.0, "next to"),
    AT_TABLE(.1, "at same table as")
    ;


    String description;
    double weight;
    SeatRelationType(double weight, String description) {
        this.weight = weight;
        this.description = description;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return description;
    }
}
