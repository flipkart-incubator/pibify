package com.flipkart.pibify.test.data.generics;

import java.util.TreeMap;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 12/10/24
 */
public class MapClassLevel8<K> extends TreeMap<K, String> {

    // This is a non-pibify container extension
    public void randomize(K key) {
        this.put(key, "str" + Math.random());
    }
}
