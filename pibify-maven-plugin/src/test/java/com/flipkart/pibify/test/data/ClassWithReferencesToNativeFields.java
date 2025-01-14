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
import com.flipkart.pibify.test.data.another.AnotherClassWithNativeFields;

/**
 * This class is used for testing Class Refs
 * Author bageshwar.pn
 * Date 10/08/24
 */
public class ClassWithReferencesToNativeFields {

    @Pibify(1)
    private AnotherClassWithNativeFields reference;

    @Pibify(2)
    private String aString;

    public ClassWithReferencesToNativeFields randomize() {
        aString = "str" + Math.random();
        reference = new AnotherClassWithNativeFields();
        reference.randomize();
        return this;
    }

    public AnotherClassWithNativeFields getReference() {
        return reference;
    }

    public void setReference(AnotherClassWithNativeFields reference) {
        this.reference = reference;
    }

    public String getaString() {
        return aString;
    }

    public void setaString(String aString) {
        this.aString = aString;
    }
}
