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
public abstract class AbstractClassWithNativeFields {

    @Pibify(1)
    public String aString;

    public AbstractClassWithNativeFields randomize() {
        aString = "str" + Math.random();
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractClassWithNativeFields)) return false;

        AbstractClassWithNativeFields that = (AbstractClassWithNativeFields) o;
        return Objects.equals(aString, that.aString);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(aString);
    }
}
