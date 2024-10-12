package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;

import java.util.Objects;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 12/10/24
 */
public class ClassWithUnresolvedGenericType<T> extends ClassWithTypeParameterReference<T> {

    @Pibify(1)
    private String str;

    public void randomize(T object) {
        str = "str" + Math.random();
        super.randomize(object);
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClassWithUnresolvedGenericType)) return false;
        if (!super.equals(o)) return false;

        ClassWithUnresolvedGenericType<?> that = (ClassWithUnresolvedGenericType<?>) o;
        return Objects.equals(str, that.str);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(str);
        return result;
    }
}
