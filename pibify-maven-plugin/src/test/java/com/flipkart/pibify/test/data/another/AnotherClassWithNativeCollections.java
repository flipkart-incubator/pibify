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

package com.flipkart.pibify.test.data.another;

import com.flipkart.pibify.core.Pibify;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * This class is used for testing native collections handling
 * Author bageshwar.pn
 * Date 09/08/24
 */
public class AnotherClassWithNativeCollections {

    @Pibify(1)
    private List<String> aString;

    @Pibify(2)
    private Set<Integer> anInt;

    @Pibify(3)
    private Map<Float, Boolean> aMap;

    public void randomize() {
        aString = Arrays.asList("str" + Math.random(), "str" + Math.random(), "str" + Math.random());
        anInt = new HashSet<>(Arrays.asList(
                (int) (Math.random() * 1000), (int) (Math.random() * 1000), (int) (Math.random() * 1000)));

        aMap = new HashMap<>();
        aMap.put((float) Math.random(), Math.random() > 0.5);
        aMap.put((float) Math.random(), Math.random() > 0.5);
        aMap.put((float) Math.random(), Math.random() > 0.5);
    }


    public List<String> getaString() {
        return aString;
    }

    public void setaString(List<String> aString) {
        this.aString = aString;
    }

    public Set<Integer> getAnInt() {
        return anInt;
    }

    public void setAnInt(Set<Integer> anInt) {
        this.anInt = anInt;
    }

    public Map<Float, Boolean> getaMap() {
        return aMap;
    }

    public void setaMap(Map<Float, Boolean> aMap) {
        this.aMap = aMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnotherClassWithNativeCollections that = (AnotherClassWithNativeCollections) o;
        return Objects.equals(aString, that.aString) && Objects.equals(anInt, that.anInt) && Objects.equals(aMap, that.aMap);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(aString);
        result = 31 * result + Objects.hashCode(anInt);
        result = 31 * result + Objects.hashCode(aMap);
        return result;
    }
}
