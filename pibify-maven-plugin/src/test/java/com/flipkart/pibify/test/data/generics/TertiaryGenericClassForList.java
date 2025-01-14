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

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 02/11/24
 */
public class TertiaryGenericClassForList<T> extends AGenericClass<List<T>> {

    public TertiaryGenericClassForList<T> randomize(T value1, T value2, T value3) {
        List<T> list = new ArrayList<>();
        list.add(value1);
        list.add(value2);
        list.add(value3);
        super.value = list;
        return this;
    }
}
