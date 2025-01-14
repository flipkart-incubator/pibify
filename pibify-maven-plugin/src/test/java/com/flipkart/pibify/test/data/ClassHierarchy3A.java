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
public class ClassHierarchy3A extends ClassHierarchy2A {

    @Pibify(1)
    private String member3;

    @Override
    public void randomize() {
        super.randomize();
        member3 = "str" + Math.random();
    }

    public String getMember3() {
        return member3;
    }

    public void setMember3(String member1) {
        this.member3 = member1;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ClassHierarchy3A)) return false;
        if (!super.equals(o)) return false;
        ClassHierarchy3A that = (ClassHierarchy3A) o;
        return Objects.equals(member3, that.member3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), member3);
    }
}
