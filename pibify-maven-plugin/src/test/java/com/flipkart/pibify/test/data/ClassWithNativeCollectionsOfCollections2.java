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

import com.flipkart.pibify.core.Pibify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Author bageshwar.pn
 * Date 15/08/24
 */
public class ClassWithNativeCollectionsOfCollections2 {

    @Pibify(1)
    private List<Set<String>> aString;

    @Pibify(2)
    private Set<List<Integer>> anInt;

    @Pibify(3)
    private Map<Float, List<Set<Map<String, Boolean>>>> aMap;

    public ClassWithNativeCollectionsOfCollections2 randomize() {
        aString = new ArrayList<>();
        aString.add(new HashSet<>(Arrays.asList("str" + Math.random(), "str" + Math.random(), "str" + Math.random())));
        aString.add(new HashSet<>(Arrays.asList("str" + Math.random(), "str" + Math.random(), "str" + Math.random())));
        aString.add(new HashSet<>(Arrays.asList("str" + Math.random(), "str" + Math.random(), "str" + Math.random())));
        anInt = new HashSet<>();
        anInt.add(Arrays.asList((int) (Math.random() * 1000), (int) (Math.random() * 1000), (int) (Math.random() * 1000)));
        anInt.add(Arrays.asList((int) (Math.random() * 1000), (int) (Math.random() * 1000), (int) (Math.random() * 1000)));
        anInt.add(Arrays.asList((int) (Math.random() * 1000), (int) (Math.random() * 1000), (int) (Math.random() * 1000)));

        aMap = new HashMap<>();
//        aMap.put((float) Math.random(), new HashMap<String, Boolean>(){{
//            put("str" + Math.random(), Math.random() > 0.5);
//            put("str" + Math.random(), Math.random() > 0.5);
//            put("str" + Math.random(), Math.random() > 0.5);
//        }});

        return this;
    }

    public List<Set<String>> getaString() {
        return aString;
    }

    public void setaString(List<Set<String>> aString) {
        this.aString = aString;
    }

    public Set<List<Integer>> getAnInt() {
        return anInt;
    }

    public void setAnInt(Set<List<Integer>> anInt) {
        this.anInt = anInt;
    }

    public Map<Float, List<Set<Map<String, Boolean>>>> getaMap() {
        return aMap;
    }

    public void setaMap(Map<Float, List<Set<Map<String, Boolean>>>> aMap) {
        this.aMap = aMap;
    }
}
