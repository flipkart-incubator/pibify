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

import java.util.Objects;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 02/11/24
 */
public class GenericClassWithMultipleParameters<A, B, C> {

    @Pibify(1)
    public A a;

    @Pibify(2)
    public B b;

    @Pibify(3)
    public C c;

    public static <X, Y, Z> GenericClassWithMultipleParameters<X, Y, Z> randomize(X x, Y y, Z z) {
        GenericClassWithMultipleParameters<X, Y, Z> result = new GenericClassWithMultipleParameters<>();
        result.a = x;
        result.b = y;
        result.c = z;
        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof GenericClassWithMultipleParameters)) return false;
        GenericClassWithMultipleParameters<?, ?, ?> that = (GenericClassWithMultipleParameters<?, ?, ?>) object;
        return Objects.equals(a, that.a) && Objects.equals(b, that.b) && Objects.equals(c, that.c);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, c);
    }
}
