package com.flipkart.pibify.test.data.generics;

import java.util.HashMap;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 27/10/24
 */
public class InheritedHashMap extends HashMap<String, String> {

    public InheritedHashMap randomize() {
        this.put("str" + Math.random(), "str" + Math.random());
        this.put("str" + Math.random(), "str" + Math.random());
        this.put("str" + Math.random(), "str" + Math.random());
        return this;
    }
}
