package com.flipkart.pibify.test.data;

import java.util.ArrayList;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 30/10/24
 */
public class Sentence extends ArrayList<String> {

    //@Pibify(1)
    //public String aString;

    public Sentence randomize() {
        this.add("str" + Math.random());
        this.add("str" + Math.random());
        this.add("str" + Math.random());
        return this;
    }
}
