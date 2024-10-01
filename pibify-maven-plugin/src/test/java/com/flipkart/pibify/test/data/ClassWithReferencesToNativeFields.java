package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;
import com.flipkart.pibify.test.data.another.AnotherClassWithNativeFields;

/**
 * This class is used for testing Class Refs
 * Author bageshwar.pn
 * Date 10/08/24
 */
public class ClassWithReferencesToNativeFields {

    @Pibify(1)
    private AnotherClassWithNativeFields reference;

    @Pibify(2)
    private String aString;

    public ClassWithReferencesToNativeFields randomize() {
        aString = "str" + Math.random();
        reference = new AnotherClassWithNativeFields();
        reference.randomize();
        return this;
    }

    public AnotherClassWithNativeFields getReference() {
        return reference;
    }

    public void setReference(AnotherClassWithNativeFields reference) {
        this.reference = reference;
    }

    public String getaString() {
        return aString;
    }

    public void setaString(String aString) {
        this.aString = aString;
    }
}
