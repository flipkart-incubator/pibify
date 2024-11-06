package com.flipkart.pibify.test.data.generics;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 02/11/24
 */
public class AnotherTertiaryGenericClass<T> extends AGenericClass<Map<String, T>> {

    public AnotherTertiaryGenericClass<T> randomize(T value1, T value2, T value3) {
        Map<String, T> map = new HashMap<>();
        map.put("str" + Math.random(), value1);
        map.put("str" + Math.random(), value2);
        map.put("str" + Math.random(), value3);
        super.value = map;
        return this;
    }
}
