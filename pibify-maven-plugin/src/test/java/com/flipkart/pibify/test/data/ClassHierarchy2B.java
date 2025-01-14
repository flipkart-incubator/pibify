package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;

import java.util.Objects;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 07/09/24
 */
public class ClassHierarchy2B extends ClassHierarchy1 {

    @Pibify(1)
    private String member1;

    @Override
    public void randomize() {
        super.randomize();
        member1 = "str" + Math.random();
    }

    public String getMember1() {
        return member1;
    }

    public void setMember1(String member1) {
        this.member1 = member1;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ClassHierarchy2B)) return false;
        if (!super.equals(o)) return false;
        ClassHierarchy2B that = (ClassHierarchy2B) o;
        return Objects.equals(member1, that.member1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), member1);
    }
}
