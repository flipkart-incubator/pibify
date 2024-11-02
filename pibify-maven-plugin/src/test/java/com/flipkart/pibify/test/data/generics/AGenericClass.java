package com.flipkart.pibify.test.data.generics;

import com.flipkart.pibify.core.Pibify;

import java.util.Objects;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 02/11/24
 */
public class AGenericClass<T> {

    @Pibify(1)
    public T value;

    public static <E> AGenericClass<E> randomize(E value) {
        AGenericClass<E> result = new AGenericClass<>();
        result.value = value;
        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof AGenericClass)) return false;
        AGenericClass<?> that = (AGenericClass<?>) object;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
