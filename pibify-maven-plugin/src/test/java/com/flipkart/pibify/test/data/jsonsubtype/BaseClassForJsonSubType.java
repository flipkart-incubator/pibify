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

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.flipkart.pibify.core.Pibify;
import com.flipkart.pibify.test.data.EnumB;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 15/12/24
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "cType")
@JsonSubTypes({
        @JsonSubTypes.Type(name = "TypeA", value = TypeAForJsonSubtypes.class),
        @JsonSubTypes.Type(name = "TypeB", value = TypeBForJsonSubtypes.class),
        @JsonSubTypes.Type(name = "TypeC", value = TypeCForJsonSubtypes.class)
})
@Data
@NoArgsConstructor
public class BaseClassForJsonSubType {

    @Pibify(1)
    private String aString;

    @Pibify(2)
    private EnumB anEnum;

    public BaseClassForJsonSubType(EnumB anEnum) {
        this.anEnum = anEnum;
    }

    public static BaseClassForJsonSubType randomize() {
        BaseClassForJsonSubType type = new BaseClassForJsonSubType(EnumB.randomize());
        type.setAString("str" + Math.random());
        return type;

    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BaseClassForJsonSubType)) return false;
        BaseClassForJsonSubType that = (BaseClassForJsonSubType) o;
        return Objects.equals(aString, that.aString) && anEnum == that.anEnum;
    }

    @Override
    public int hashCode() {
        return Objects.hash(aString, anEnum);
    }
}
