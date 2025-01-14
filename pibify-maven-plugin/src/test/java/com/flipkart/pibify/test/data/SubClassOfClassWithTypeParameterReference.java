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
 * Date 20/09/24
 */
public class SubClassOfClassWithTypeParameterReference
        extends ClassWithTypeParameterReference<ClassWithReferences> {

    @Pibify(1)
    String str;

    public void randomize() {
        str = "str" + Math.random();
        super.randomize(new ClassWithReferences().randomize());
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubClassOfClassWithTypeParameterReference that = (SubClassOfClassWithTypeParameterReference) o;
        return Objects.equals(str, that.str) && super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(str);
    }
}
