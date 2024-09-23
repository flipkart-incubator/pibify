package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;
import com.flipkart.pibify.test.data.another.AnotherClassWithNativeCollections;

import java.util.Objects;

/**
 * This class is used for testing Class Refs
 * Author bageshwar.pn
 * Date 10/08/24
 */
public class ClassWithReferences {

    @Pibify(1)
    private AnotherClassWithNativeCollections reference;

    @Pibify(2)
    private String aString;

    public ClassWithReferences randomize() {
        aString = "str" + Math.random();
        reference = new AnotherClassWithNativeCollections();
        reference.randomize();
        return this;
    }

    public AnotherClassWithNativeCollections getReference() {
        return reference;
    }

    public void setReference(AnotherClassWithNativeCollections reference) {
        this.reference = reference;
    }

    public String getaString() {
        return aString;
    }

    public void setaString(String aString) {
        this.aString = aString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassWithReferences that = (ClassWithReferences) o;
        return Objects.equals(reference, that.reference) && Objects.equals(aString, that.aString);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(reference);
        result = 31 * result + Objects.hashCode(aString);
        return result;
    }
}
