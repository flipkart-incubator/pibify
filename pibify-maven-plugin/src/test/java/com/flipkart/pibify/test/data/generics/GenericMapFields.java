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

        inheritedHashMap = new InheritedHashMap();
        inheritedHashMap.put("str" + Math.random(), "str" + Math.random());
        inheritedHashMap.put("str" + Math.random(), "str" + Math.random());
        inheritedHashMap.put("str" + Math.random(), "str" + Math.random());

        inheritedSet = new InheritedTreeSet();
        inheritedSet.add(Math.random());
        inheritedSet.add(Math.random());
        inheritedSet.add(Math.random());
    }
}
