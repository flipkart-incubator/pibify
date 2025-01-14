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
import com.flipkart.pibify.test.data.generics.InheritedHashMap;
import com.flipkart.pibify.test.data.generics.InheritedTreeSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 30/10/24
 */
public class ClassForNestedReferenceList {

    @Pibify(1)
    public List<Sentence> words;

    @Pibify(2)
    public List<ArrayList<String>> listOfArrayLists;

    @Pibify(3)
    public Map<InheritedHashMap, InheritedHashMap> mapOfMapsAndMaps;

    @Pibify(4)
    public Map<InheritedHashMap, InheritedTreeSet> mapOfMapsAndSets;

    public void randomize() {
        words = new ArrayList<>();
        words.add(new Sentence().randomize());
        words.add(new Sentence().randomize());
        words.add(new Sentence().randomize());

        listOfArrayLists = new ArrayList<>();
        listOfArrayLists.add(new Sentence().randomize());
        listOfArrayLists.add(new Sentence().randomize());
        // setting a impl for testing
        listOfArrayLists.add(new ArrayList<>());

        mapOfMapsAndMaps = new HashMap<>();
        mapOfMapsAndMaps.put(new InheritedHashMap().randomize(), new InheritedHashMap().randomize());
        mapOfMapsAndMaps.put(new InheritedHashMap().randomize(), new InheritedHashMap().randomize());
        mapOfMapsAndMaps.put(new InheritedHashMap().randomize(), new InheritedHashMap().randomize());

        mapOfMapsAndSets = new HashMap<>();
        mapOfMapsAndSets.put(new InheritedHashMap().randomize(), new InheritedTreeSet().randomize());
        mapOfMapsAndSets.put(new InheritedHashMap().randomize(), new InheritedTreeSet().randomize());
        mapOfMapsAndSets.put(new InheritedHashMap().randomize(), new InheritedTreeSet().randomize());
    }
}

