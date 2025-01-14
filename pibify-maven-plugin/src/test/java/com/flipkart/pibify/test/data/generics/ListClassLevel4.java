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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 12/10/24
 */
public class ListClassLevel4<T, V> extends ArrayList<List<V>> {

    @Pibify(1)
    private String str;

    @Pibify(2)
    private T obj;

    public void randomize(T key, V value) {
        str = "str" + Math.random();
        obj = key;
        this.add(Arrays.asList(value));
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
