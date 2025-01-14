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
 * Date 27/10/24
 */
public class ConcreteClassBWithNativeFields extends AbstractClassWithNativeFields {

    @Pibify(1)
    public double aDouble1;

    public ConcreteClassBWithNativeFields randomize() {
        super.randomize();
        aDouble1 = Math.random();
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConcreteClassBWithNativeFields)) return false;
        if (!super.equals(o)) return false;
        ConcreteClassBWithNativeFields that = (ConcreteClassBWithNativeFields) o;
        return Double.compare(aDouble1, that.aDouble1) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), aDouble1);
    }
}
