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
package com.sesquipedalian_dev.util;

/**
 * DemographicAgeBrackets - provides some utility to assign ages to a 'demographic' bracket
 */
public class DemographicAgeBrackets {
    public static int getDemographicAgeBracket(int age) {
        if(age >= 75) {
            return 7; // in the home
        } else if(age >= 60) {
            return 6; // retirees
        } else if (age >= 35) {
            return 5; // 'middle age'
        } else if (age > 22) {
            return 4; // 20-something
        } else if (age >= 18) {
            return 3; // college
        } else if (age > 12) {
            return 2; // teens
        } else if (age > 5) {
            return 1; // school age
        } else {
            return 0; // babies
        }
    }
}
