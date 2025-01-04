package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;

import java.util.Objects;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 07/09/24
 */
public class ClassHierarchy2A extends ClassHierarchy1 {

    @Pibify(1)
    String member2;

    @Pibify(2)
    private Object obj1;

    @Override
    public void randomize() {
        super.randomize();
        super.setObj1(null);
        member2 = "str" + Math.random();
        this.obj1 = "str" + Math.random();
    }

    public String getMember2() {
        return member2;
    }

    public void setMember2(String member2) {
        this.member2 = member2;
    }

    @Override
    public Object getObj1() {
        return obj1;
    }

    @Override
    public void setObj1(Object obj1) {
        this.obj1 = obj1;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ClassHierarchy2A)) return false;
        if (!super.equals(o)) return false;
        ClassHierarchy2A that = (ClassHierarchy2A) o;
        return Objects.equals(member2, that.member2) && Objects.equals(obj1, that.obj1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), member2, obj1);
    }
}
