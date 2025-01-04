package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ClassHierarchy3A)) return false;
        if (!super.equals(o)) return false;
        ClassHierarchy3A that = (ClassHierarchy3A) o;
        return Objects.equals(member3, that.member3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), member3);
    }
}
