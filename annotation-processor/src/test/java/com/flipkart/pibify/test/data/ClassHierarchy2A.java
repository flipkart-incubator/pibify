package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 07/09/24
 */
public class ClassHierarchy2A extends ClassHierarchy1 {

    @Pibify(1)
    String member2;

    @Override
    public void randomize() {
        super.randomize();
        member2 = "str" + Math.random();
    }

    public String getMember2() {
        return member2;
    }

    public void setMember2(String member2) {
        this.member2 = member2;
    }
}
