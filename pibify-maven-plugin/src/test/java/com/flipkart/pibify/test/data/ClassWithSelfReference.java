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
 * Date 04/01/25
 */
public class ClassWithSelfReference {

    @Pibify(1)
    public String str3;

    @Pibify(2)
    public ClassWithSelfReference self;

    @Pibify(3)
    public Object objectReference;

    public static ClassWithSelfReference randomize() {
        ClassWithSelfReference classWithSelfReference = new ClassWithSelfReference();
        classWithSelfReference.str3 = "str" + Math.random();
        classWithSelfReference.self = null;
        classWithSelfReference.objectReference = "str" + Math.random();

        return classWithSelfReference;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ClassWithSelfReference)) return false;
        ClassWithSelfReference that = (ClassWithSelfReference) o;
        return Objects.equals(str3, that.str3) && Objects.equals(self, that.self) && Objects.equals(objectReference, that.objectReference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(str3, self, objectReference);
    }
}
