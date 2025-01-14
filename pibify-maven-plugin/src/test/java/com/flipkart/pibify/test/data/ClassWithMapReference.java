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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 23/09/24
 */
public class ClassWithMapReference {

    @Pibify(2)
    public Object treeMapReference;
    @Pibify(3)
    public Object linkedHashMapReference;
    @Pibify(1)
    Object hashMapReference;

    public void randomize() {
        HashMap<Object, Object> map = new HashMap<>();
        map.put(Math.random() * 10, "str" + Math.random() * 10);
        map.put(Math.random() * 10, "str" + Math.random() * 10);
        map.put(Math.random() * 10, "str" + Math.random() * 10);
        hashMapReference = map;

        treeMapReference = new TreeMap<>(map);
        linkedHashMapReference = new LinkedHashMap<>(map);
    }

    public Object getHashMapReference() {
        return hashMapReference;
    }

    public void setHashMapReference(Object hashMapReference) {
        this.hashMapReference = hashMapReference;
    }
}
