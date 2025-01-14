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

package com.flipkart.pibify.test.data.jsonsubtype;

import com.flipkart.pibify.core.Pibify;
import com.flipkart.pibify.test.data.EnumB;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 15/12/24
 */
@NoArgsConstructor
public class TypeAForJsonSubtypes extends BaseClassForJsonSubType {

    @Pibify(1)
    private String typeAString;

    public TypeAForJsonSubtypes(String typeAString) {
        super(EnumB.A);
        this.typeAString = typeAString;
    }

    public static TypeAForJsonSubtypes randomize() {
        TypeAForJsonSubtypes subType = new TypeAForJsonSubtypes("str" + Math.random());
        subType.setAString("str" + Math.random());
        return subType;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TypeAForJsonSubtypes)) return false;
        if (!super.equals(o)) return false;
        TypeAForJsonSubtypes that = (TypeAForJsonSubtypes) o;
        return Objects.equals(typeAString, that.typeAString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), typeAString);
    }

    public String getTypeAString() {
        return typeAString;
    }

    public void setTypeAString(String typeAString) {
        this.typeAString = typeAString;
    }
}
