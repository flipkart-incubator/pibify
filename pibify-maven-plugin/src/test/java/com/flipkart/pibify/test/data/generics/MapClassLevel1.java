package com.flipkart.pibify.test.data.generics;

import com.flipkart.pibify.core.Pibify;

import java.util.HashMap;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 12/10/24
 */
public class MapClassLevel1<K, V> extends HashMap<K, V> {

    @Pibify(1)
    private String str;

    public void randomize(K key, V value) {
        str = "str" + Math.random();
        this.put(key, value);
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
