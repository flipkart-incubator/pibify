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
import lombok.Data;

import java.util.Objects;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 30/09/24
 */
@Data
public class ClassWithInterestingFieldNames {

    @Pibify(1)
    private String a;

    @Pibify(2)
    private String anApple;

    @Pibify(3)
    private String aMango;

    @Pibify(4)
    private String a1;

    @Pibify(5)
    private String a1Apple;

    public void randomize() {
        a = "str" + Math.random();
        anApple = "str2" + Math.random();
        aMango = "str2" + Math.random();
        a1 = "str2" + Math.random();
        a1Apple = "str2" + Math.random();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassWithInterestingFieldNames that = (ClassWithInterestingFieldNames) o;
        return Objects.equals(a, that.a) && Objects.equals(anApple, that.anApple) && Objects.equals(aMango, that.aMango) && Objects.equals(a1, that.a1) && Objects.equals(a1Apple, that.a1Apple);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(a);
        result = 31 * result + Objects.hashCode(anApple);
        result = 31 * result + Objects.hashCode(aMango);
        result = 31 * result + Objects.hashCode(a1);
        result = 31 * result + Objects.hashCode(a1Apple);
        return result;
    }
}
