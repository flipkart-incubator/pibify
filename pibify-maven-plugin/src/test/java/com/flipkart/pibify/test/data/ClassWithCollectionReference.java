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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 23/09/24
 */
public class ClassWithCollectionReference {

    @Pibify(2)
    public Object arrayListReference;

    @Pibify(3)
    public Object queueReference;

    @Pibify(4)
    public Object stackReference;

    @Pibify(5)
    public Object vectorReference;

    @Pibify(6)
    public Object hashSetReference;

    @Pibify(7)
    public Object treeSetReference;

    @Pibify(8)
    public Object linkedHashSetReference;

    public void randomize() {
        ArrayList<Object> list = new ArrayList<>();
        list.add("Str" + Math.random() * 10);
        arrayListReference = list;

        queueReference = new ArrayDeque<>(list);
        Stack<Object> stack = new Stack<>();
        stack.addAll(list);
        stackReference = stack;

        vectorReference = new Vector<>(list);
        hashSetReference = new HashSet<>(list);
        treeSetReference = new TreeSet<>(list);
        linkedHashSetReference = new LinkedHashSet<>(list);
    }
}
