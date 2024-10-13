package com.flipkart.pibify.test.data.generics;

import com.flipkart.pibify.core.Pibify;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 12/10/24
 */
public class MapClassLevel5 extends MapClassLevel3<Double> {

    @Pibify(1)
    private String str2;

    public void randomize() {
        str2 = "str" + Math.random();
        this.put(Math.random(), "str" + Math.random());
    }

    public String getStr2() {
        return str2;
    }

    public void setStr2(String str2) {
        this.str2 = str2;
    }
}
