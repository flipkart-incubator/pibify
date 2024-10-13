package com.flipkart.pibify.test.data.generics;

import com.flipkart.pibify.core.Pibify;

import java.util.TreeMap;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 12/10/24
 */
public class MapClassLevel2<V> extends TreeMap<String, V> {

    @Pibify(1)
    private String str;

    public void randomize(V value) {
        str = "str" + Math.random();
        this.put("str" + Math.random(), value);
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
