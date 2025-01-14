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
public class ClassWithNestedCollections {

    @Pibify(1)
    private List<List<String>> aString;

    @Pibify(2)
    private Set<Set<Integer>> anInt;

    @Pibify(3)
    private Set<List<Integer>> collection2;

    @Pibify(4)
    private List<Set<Integer>> collection3;

    @Pibify(5)
    private ClassWithReferences ref1;

    @Pibify(6)
    private Map<Float, Map<String, Boolean>> aMap;

    @Pibify(7)
    private Map<Float, Map<String, Map<Integer, Boolean>>> map2;

    @Pibify(8)
    private Map<Map<Boolean, String>, Map<Integer, Boolean>> map3;

    @Pibify(9)
    private List<Map<String, Float>> listOfMaps;

    @Pibify(10)
    private Set<Map<String, Float>> setOfMaps;

    @Pibify(11)
    private Map<List<String>, Set<Boolean>> mapOfCollections;


    public ClassWithNestedCollections randomize() {
        aString = new ArrayList<>();
        aString.add(Arrays.asList("str" + Math.random(), "str" + Math.random(), "str" + Math.random()));
        aString.add(Arrays.asList("str" + Math.random(), "str" + Math.random(), "str" + Math.random()));
        aString.add(Arrays.asList("str" + Math.random(), "str" + Math.random(), "str" + Math.random()));
        anInt = new HashSet<>();
        anInt.add(new HashSet<>(Arrays.asList((int) (Math.random() * 1000), (int) (Math.random() * 1000), (int) (Math.random() * 1000))));
        anInt.add(new HashSet<>(Arrays.asList((int) (Math.random() * 1000), (int) (Math.random() * 1000), (int) (Math.random() * 1000))));
        anInt.add(new HashSet<>(Arrays.asList((int) (Math.random() * 1000), (int) (Math.random() * 1000), (int) (Math.random() * 1000))));

        aMap = new HashMap<>();
        aMap.put((float) Math.random(), new HashMap<String, Boolean>() {{
            put("str" + Math.random(), Math.random() > 0.5);
            put("str" + Math.random(), Math.random() > 0.5);
            put("str" + Math.random(), Math.random() > 0.5);
        }});

        return this;
    }

    public List<List<String>> getaString() {
        return aString;
    }

    public void setaString(List<List<String>> aString) {
        this.aString = aString;
    }

    public Set<Set<Integer>> getAnInt() {
        return anInt;
    }

    public void setAnInt(Set<Set<Integer>> anInt) {
        this.anInt = anInt;
    }

    public Map<Float, Map<String, Boolean>> getaMap() {
        return aMap;
    }

    public void setaMap(Map<Float, Map<String, Boolean>> aMap) {
        this.aMap = aMap;
    }

    public Set<List<Integer>> getCollection2() {
        return collection2;
    }

    public void setCollection2(Set<List<Integer>> collection2) {
        this.collection2 = collection2;
    }

    public List<Set<Integer>> getCollection3() {
        return collection3;
    }

    public void setCollection3(List<Set<Integer>> collection3) {
        this.collection3 = collection3;
    }

    public ClassWithReferences getRef1() {
        return ref1;
    }

    public void setRef1(ClassWithReferences ref1) {
        this.ref1 = ref1;
    }

    public Map<Float, Map<String, Map<Integer, Boolean>>> getMap2() {
        return map2;
    }

    public void setMap2(Map<Float, Map<String, Map<Integer, Boolean>>> map2) {
        this.map2 = map2;
    }

    public Map<Map<Boolean, String>, Map<Integer, Boolean>> getMap3() {
        return map3;
    }

    public void setMap3(Map<Map<Boolean, String>, Map<Integer, Boolean>> map3) {
        this.map3 = map3;
    }

    public List<Map<String, Float>> getListOfMaps() {
        return listOfMaps;
    }

    public void setListOfMaps(List<Map<String, Float>> listOfMaps) {
        this.listOfMaps = listOfMaps;
    }

    public Set<Map<String, Float>> getSetOfMaps() {
        return setOfMaps;
    }

    public void setSetOfMaps(Set<Map<String, Float>> setOfMaps) {
        this.setOfMaps = setOfMaps;
    }

    public Map<List<String>, Set<Boolean>> getMapOfCollections() {
        return mapOfCollections;
    }

    public void setMapOfCollections(Map<List<String>, Set<Boolean>> mapOfCollections) {
        this.mapOfCollections = mapOfCollections;
    }
}
