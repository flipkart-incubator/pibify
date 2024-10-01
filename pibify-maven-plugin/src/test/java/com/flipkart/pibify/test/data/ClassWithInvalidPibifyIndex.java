package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 13/09/24
 */
public class ClassWithInvalidPibifyIndex {
    @Pibify(1)
    String a;
    @Pibify(1)
    String b;
    @Pibify(-1)
    String c;

    @Pibify(130)
    String d;

    @Pibify(0)
    String e;
}
