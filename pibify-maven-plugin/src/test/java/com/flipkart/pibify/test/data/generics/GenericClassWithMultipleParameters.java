package com.flipkart.pibify.test.data.generics;

import com.flipkart.pibify.core.Pibify;

import java.util.Objects;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 02/11/24
 */
public class GenericClassWithMultipleParameters<A, B, C> {

    @Pibify(1)
    public A a;

    @Pibify(2)
    public B b;

    @Pibify(3)
    public C c;

    public static <X, Y, Z> GenericClassWithMultipleParameters<X, Y, Z> randomize(X x, Y y, Z z) {
        GenericClassWithMultipleParameters<X, Y, Z> result = new GenericClassWithMultipleParameters<>();
        result.a = x;
        result.b = y;
        result.c = z;
        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof GenericClassWithMultipleParameters)) return false;
        GenericClassWithMultipleParameters<?, ?, ?> that = (GenericClassWithMultipleParameters<?, ?, ?>) object;
        return Objects.equals(a, that.a) && Objects.equals(b, that.b) && Objects.equals(c, that.c);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, c);
    }
}
