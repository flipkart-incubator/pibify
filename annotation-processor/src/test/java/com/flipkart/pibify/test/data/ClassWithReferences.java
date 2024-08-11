package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;
import com.flipkart.pibify.test.data.another.AnotherClassWithNativeCollections;

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
}
