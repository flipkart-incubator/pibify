package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;

import java.util.Objects;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 27/10/24
 */
public class ConcreteClassWithNativeFields extends AbstractClassWithNativeFields {

    @Pibify(1)
    public double aDouble;

    public ConcreteClassWithNativeFields randomize() {
        super.randomize();
        aDouble = Math.random();
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConcreteClassWithNativeFields)) return false;
        if (!super.equals(o)) return false;
        ConcreteClassWithNativeFields that = (ConcreteClassWithNativeFields) o;
        return Double.compare(aDouble, that.aDouble) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), aDouble);
    }
}
