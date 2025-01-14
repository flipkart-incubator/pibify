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

package com.flipkart.pibify.test.data.generics;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 02/11/24
 */
public class AnotherTertiaryGenericClass<T> extends AGenericClass<Map<String, T>> {

    public AnotherTertiaryGenericClass<T> randomize(T value1, T value2, T value3) {
        Map<String, T> map = new HashMap<>();
        map.put("str" + Math.random(), value1);
        map.put("str" + Math.random(), value2);
        map.put("str" + Math.random(), value3);
        super.value = map;
        return this;
    }
}
