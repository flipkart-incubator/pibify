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

import com.flipkart.pibify.core.Pibify;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 02/11/24
 */
public class AnotherGenericClass<T> {

    @Pibify(1)
    public Map<Double, List<AGenericClass<T>>> list;

    public void randomize(T value1, T value2, T value3) {
        this.list = new HashMap<>();
        this.list.put(Math.random(), Collections.singletonList(AGenericClass.randomize(value1)));
        this.list.put(Math.random(), Collections.singletonList(AGenericClass.randomize(value2)));
        this.list.put(Math.random(), Collections.singletonList(AGenericClass.randomize(value3)));
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof AnotherGenericClass)) return false;
        AnotherGenericClass<?> that = (AnotherGenericClass<?>) object;
        return Objects.equals(list, that.list);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(list);
    }
}
