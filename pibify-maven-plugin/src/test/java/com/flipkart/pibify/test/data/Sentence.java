/*
 *
 *  *Copyright [2025] [Original Author]
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.flipkart.pibify.test.data;

import java.util.ArrayList;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 30/10/24
 */
public class Sentence extends ArrayList<String> {

    //@Pibify(1)
    //public String aString;

    public Sentence randomize() {
        this.add("str" + Math.random());
        this.add("str" + Math.random());
        this.add("str" + Math.random());
        return this;
    }
}
