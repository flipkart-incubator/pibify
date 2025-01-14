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

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 07/09/24
 */
public class ClassWithEnums {

    @Pibify(1)
    private EnumA enumA;
    @Pibify(2)
    private EnumB enumB;
    @Pibify(3)
    private EnumB nullEnum = null;

    public void randomize() {
        enumA = EnumA.values()[((int) (Math.random() * 10)) % 3];
        enumB = EnumB.values()[((int) (Math.random() * 10)) % 3];
    }

    public EnumA getEnumA() {
        return enumA;
    }

    public void setEnumA(EnumA enumA) {
        this.enumA = enumA;
    }

    public EnumB getEnumB() {
        return enumB;
    }

    public void setEnumB(EnumB enumB) {
        this.enumB = enumB;
    }

    public EnumB getNullEnum() {
        return nullEnum;
    }

    public void setNullEnum(EnumB nullEnum) {
        this.nullEnum = nullEnum;
    }

    public enum EnumA {
        A, B, C
    }
}
