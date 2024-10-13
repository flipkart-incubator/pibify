package com.flipkart.pibify.test.data.generics;

import com.flipkart.pibify.core.Pibify;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 12/10/24
 */
public class MapClassLevel4<K, V> extends MapClassLevel1<K, V> {

    @Pibify(1)
    private String str1;

    public void randomize() {
        str1 = "str" + Math.random();
    }

    public String getStr1() {
        return str1;
    }

    public void setStr1(String str) {
        this.str1 = str;
    }
}
