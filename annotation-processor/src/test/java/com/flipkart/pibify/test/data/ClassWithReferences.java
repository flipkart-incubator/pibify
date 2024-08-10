package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;

/**
 * This class is used for testing Class Refs
 * Author bageshwar.pn
 * Date 10/08/24
 */
public class ClassWithReferences {

    @Pibify(1)
    private ClassWithNativeCollections reference;

    public ClassWithNativeCollections getReference() {
        return reference;
    }

    public void setReference(ClassWithNativeCollections reference) {
        this.reference = reference;
    }
}
