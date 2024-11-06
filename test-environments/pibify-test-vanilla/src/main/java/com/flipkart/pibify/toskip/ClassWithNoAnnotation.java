package com.flipkart.pibify.toskip;

import com.flipkart.pibify.core.Pibify;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 23/09/24
 */
public class ClassWithNoAnnotation {

    @Pibify(value = 1)
    public String publicString;

    @Pibify(value = 2)
    protected String protectedString;

    @Pibify(value = 3)
    private String privateString;
}
