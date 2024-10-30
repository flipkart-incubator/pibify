package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;

import java.util.Objects;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 27/10/24
 */
public abstract class AbstractClassWithNativeFields {

    @Pibify(1)
    public String aString;

    public AbstractClassWithNativeFields randomize() {
        aString = "str" + Math.random();
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractClassWithNativeFields)) return false;

        AbstractClassWithNativeFields that = (AbstractClassWithNativeFields) o;
        return Objects.equals(aString, that.aString);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(aString);
    }
}
