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

import java.math.BigDecimal;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 19/10/24
 */
public class GenericMapFields {

    @Pibify(1)
    public MapClassLevel3<Double> doubleToString;

    @Pibify(2)
    public MapClassLevel2<Double> stringToDouble;

    @Pibify(3)
    public MapClassLevel3<BigDecimal> bigDecimalToString;

    @Pibify(4)
    public MapClassLevel2<BigDecimal> stringToBigDecimal;

    @Pibify(5)
    public MapClassLevel7<Double> l7Double;

    @Pibify(6)
    public MapClassLevel7<BigDecimal> l7BigDecimal;

    @Pibify(7)
    public InheritedHashMap inheritedHashMap;

    @Pibify(8)
    public InheritedTreeSet inheritedSet;

    @Pibify(9)
    public MapClassLevel9<Double, Double> inheritedMap2;

    @Pibify(10)
    public MapClassLevel7<AnotherTertiaryGenericClass<String>> l7MapOfReferences;

    public void randomize() {
        doubleToString = new MapClassLevel3<>();
        doubleToString.randomize(Math.random());

        bigDecimalToString = new MapClassLevel3<>();
        bigDecimalToString.randomize(BigDecimal.valueOf(Math.random()));

        stringToDouble = new MapClassLevel2<>();
        stringToDouble.randomize(Math.random());

        stringToBigDecimal = new MapClassLevel2<>();
        stringToBigDecimal.randomize(BigDecimal.valueOf(Math.random()));

        l7Double = new MapClassLevel7<>();
        l7Double.randomize(Math.random());

        l7BigDecimal = new MapClassLevel7<>();
        l7BigDecimal.randomize(BigDecimal.valueOf(Math.random()));

        inheritedHashMap = new InheritedHashMap().randomize();

        inheritedSet = new InheritedTreeSet().randomize();

        inheritedMap2 = new MapClassLevel9<>();
        inheritedMap2.put(Math.random(), Math.random());
        inheritedMap2.put(Math.random(), Math.random());
        inheritedMap2.put(Math.random(), Math.random());

        l7MapOfReferences = new MapClassLevel7<>();
        l7MapOfReferences.randomize(new AnotherTertiaryGenericClass<String>().randomize(
                "str" + Math.random(), "str" + Math.random(), "str" + Math.random()));
    }
}
