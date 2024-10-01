package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;

import java.util.Objects;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 20/09/24
 */
public class ClassWithTypeParameterReference<T> {

    @Pibify(1)
    T genericTypeReference;

    public T getGenericTypeReference() {
        return genericTypeReference;
    }

    public void setGenericTypeReference(T genericTypeReference) {
        this.genericTypeReference = genericTypeReference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassWithTypeParameterReference<?> that = (ClassWithTypeParameterReference<?>) o;
        return Objects.equals(genericTypeReference, that.genericTypeReference);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(genericTypeReference);
    }
}
