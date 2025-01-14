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

import java.util.Objects;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 07/09/24
 */
abstract public class ClassHierarchy1 {

    @Pibify(3)
    public String str3;
    @Pibify(2)
    protected String str2;
    @Pibify(4)
    String str4;
    @Pibify(1)
    private String str1;

    @Pibify(5)
    private Object obj1;

    public void randomize() {
        str1 = "str" + Math.random();
        str2 = "str" + Math.random();
        str3 = "str" + Math.random();
        str4 = "str" + Math.random();
        obj1 = "str" + Math.random();
    }

    public String getStr1() {
        return str1;
    }

    public void setStr1(String str1) {
        this.str1 = str1;
    }

    public String getStr2() {
        return str2;
    }

    public void setStr2(String str2) {
        this.str2 = str2;
    }

    public String getStr3() {
        return str3;
    }

    public void setStr3(String str3) {
        this.str3 = str3;
    }

    public String getStr4() {
        return str4;
    }

    public void setStr4(String str4) {
        this.str4 = str4;
    }

    public Object getObj1() {
        return obj1;
    }

    public void setObj1(Object obj1) {
        this.obj1 = obj1;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ClassHierarchy1)) return false;
        ClassHierarchy1 that = (ClassHierarchy1) o;
        return Objects.equals(str3, that.str3) && Objects.equals(str2, that.str2) && Objects.equals(str4, that.str4) && Objects.equals(str1, that.str1) && Objects.equals(obj1, that.obj1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(str3, str2, str4, str1, obj1);
    }
}
