package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 07/09/24
 */
public class ClassHierarchy3A extends ClassHierarchy2A {

    @Pibify(1)
    private String member3;

    @Override
    public void randomize() {
        super.randomize();
        member3 = "str" + Math.random();
    }

    public String getMember3() {
        return member3;
    }

    public void setMember3(String member1) {
        this.member3 = member1;
    }
}
