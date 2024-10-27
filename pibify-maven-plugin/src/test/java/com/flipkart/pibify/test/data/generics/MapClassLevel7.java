package com.flipkart.pibify.test.data.generics;

import java.util.TreeMap;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 12/10/24
 */
public class MapClassLevel7<V> extends TreeMap<String, V> {

    // This is a non-pibify container extension

    public void randomize(V value) {
        this.put("str" + Math.random(), value);
    }
}
