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

package com.flipkart.pibify.test.data.jsoncreator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flipkart.pibify.core.Pibify;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 08/12/24
 */
public class DuplicatePropertyNames {

    @Pibify(1)
    private String aString;

    @Pibify(2)
    private String aString1;

    @Pibify(3)
    private Double aDouble;

    @JsonCreator
    public DuplicatePropertyNames(@JsonProperty("aString") String aString,
                                  @JsonProperty("aString") String aString1,
                                  @JsonProperty("aDouble") Double aDouble) {
        this.aString = aString;
        this.aString1 = aString1;
        this.aDouble = aDouble;
    }

    public String getaString() {
        return aString;
    }

    public String getaString1() {
        return aString1;
    }

    public Double getaDouble() {
        return aDouble;
    }
}
