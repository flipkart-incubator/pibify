package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;

import java.util.Objects;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 27/10/24
 */
public class ConcreteClassBWithNativeFields extends AbstractClassWithNativeFields {

    @Pibify(1)
    public double aDouble1;

    public ConcreteClassBWithNativeFields randomize() {
        super.randomize();
        aDouble1 = Math.random();
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConcreteClassBWithNativeFields)) return false;
        if (!super.equals(o)) return false;
        ConcreteClassBWithNativeFields that = (ConcreteClassBWithNativeFields) o;
        return Double.compare(aDouble1, that.aDouble1) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), aDouble1);
    }
}
