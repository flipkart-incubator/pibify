package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 12/11/24
 */
public class ClassWithBasicList {

    @Pibify(1)
    public List<Boolean> list;

    public void randomize() {
        list = new ArrayList<>();
        list.add(true);
        list.add(false);
        list.add(true);
    }
}
