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
public class TypeCForJsonSubtypes extends BaseClassForJsonSubType {

    @Pibify(3)
    private String typeCString;

    public TypeCForJsonSubtypes(String typeCString) {
        super(EnumB.C);
        this.typeCString = typeCString;
    }

    public static TypeCForJsonSubtypes randomize() {
        TypeCForJsonSubtypes subtype = new TypeCForJsonSubtypes("str" + Math.random());
        subtype.setAString("str" + Math.random());
        return subtype;
    }

    public String getTypeCString() {
        return typeCString;
    }

    public void setTypeCString(String typeCString) {
        this.typeCString = typeCString;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TypeCForJsonSubtypes)) return false;
        if (!super.equals(o)) return false;
        TypeCForJsonSubtypes that = (TypeCForJsonSubtypes) o;
        return Objects.equals(typeCString, that.typeCString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), typeCString);
    }
}
