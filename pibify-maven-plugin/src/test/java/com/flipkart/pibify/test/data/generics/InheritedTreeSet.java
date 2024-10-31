package com.flipkart.pibify.test.data.generics;

import java.util.TreeSet;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 27/10/24
 */
public class InheritedTreeSet extends TreeSet<Double> {

    public InheritedTreeSet randomize() {
        this.add(Math.random());
        this.add(Math.random());
        this.add(Math.random());
        return this;
    }
}
