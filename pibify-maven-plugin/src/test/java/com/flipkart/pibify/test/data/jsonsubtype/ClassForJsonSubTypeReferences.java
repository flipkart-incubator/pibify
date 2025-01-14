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
import com.pibify.shaded.com.google.common.collect.ImmutableMap;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 15/12/24
 */
public class ClassForJsonSubTypeReferences {

    @Pibify(1)
    public BaseClassForJsonSubType baseClassForJsonSubType;

    @Pibify(2)
    public BaseClassForJsonSubType subClassForJsonSubType;

    @Pibify(3)
    public List<BaseClassForJsonSubType> baseClassesForJsonSubType;
    @Pibify(4)
    public List<BaseClassForJsonSubType> subClassesForJsonSubType;

    @Pibify(5)
    public Map<EnumB, BaseClassForJsonSubType> baseClassForJsonSubTypeMap;
    @Pibify(6)
    public Map<EnumB, BaseClassForJsonSubType> subClassForJsonSubTypeMap;

    public static ClassForJsonSubTypeReferences randomize() {
        ClassForJsonSubTypeReferences classForJsonSubTypeReferences = new ClassForJsonSubTypeReferences();
        classForJsonSubTypeReferences.baseClassForJsonSubType = BaseClassForJsonSubType.randomize();
        classForJsonSubTypeReferences.subClassForJsonSubType = TypeAForJsonSubtypes.randomize();

        classForJsonSubTypeReferences.baseClassesForJsonSubType = Arrays.asList(BaseClassForJsonSubType.randomize(), BaseClassForJsonSubType.randomize());
        classForJsonSubTypeReferences.subClassesForJsonSubType = Arrays.asList(TypeAForJsonSubtypes.randomize(), TypeBForJsonSubtypes.randomize(), TypeCForJsonSubtypes.randomize());

        classForJsonSubTypeReferences.baseClassForJsonSubTypeMap = ImmutableMap.of(EnumB.A, BaseClassForJsonSubType.randomize(), EnumB.B, BaseClassForJsonSubType.randomize(), EnumB.C, BaseClassForJsonSubType.randomize());
        classForJsonSubTypeReferences.subClassForJsonSubTypeMap = ImmutableMap.of(EnumB.A, TypeAForJsonSubtypes.randomize(), EnumB.B, TypeBForJsonSubtypes.randomize(), EnumB.C, TypeCForJsonSubtypes.randomize());

        return classForJsonSubTypeReferences;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ClassForJsonSubTypeReferences)) return false;
        ClassForJsonSubTypeReferences that = (ClassForJsonSubTypeReferences) o;
        return Objects.equals(baseClassForJsonSubType, that.baseClassForJsonSubType) && Objects.equals(subClassForJsonSubType, that.subClassForJsonSubType) && Objects.equals(baseClassesForJsonSubType, that.baseClassesForJsonSubType) && Objects.equals(subClassesForJsonSubType, that.subClassesForJsonSubType) && Objects.equals(baseClassForJsonSubTypeMap, that.baseClassForJsonSubTypeMap) && Objects.equals(subClassForJsonSubTypeMap, that.subClassForJsonSubTypeMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseClassForJsonSubType, subClassForJsonSubType, baseClassesForJsonSubType, subClassesForJsonSubType, baseClassForJsonSubTypeMap, subClassForJsonSubTypeMap);
    }
}
