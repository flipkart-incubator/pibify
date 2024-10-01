package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;

import java.util.HashMap;
import java.util.Objects;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 23/09/24
 */
public class ClassWithMapReference {

    @Pibify(1)
    Object collectionReference;

    public void randomize() {
        HashMap<Object, Object> map = new HashMap<>();
        map.put(Math.random() * 10, "str" + Math.random() * 10);
        collectionReference = map;
    }

    public Object getCollectionReference() {
        return collectionReference;
    }

    public void setCollectionReference(Object collectionReference) {
        this.collectionReference = collectionReference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassWithMapReference that = (ClassWithMapReference) o;
        return Objects.equals(collectionReference, that.collectionReference);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(collectionReference);
    }
}
