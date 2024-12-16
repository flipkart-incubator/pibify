package com.flipkart.pibify;

import com.flipkart.pibify.core.Pibify;

import java.util.ArrayList;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 23/09/24
 */
public class ClassWithCollectionReference {

    @Pibify(value = 1)
    Object collectionReference;

    public void randomize() {
        ArrayList<Object> list = new ArrayList<>();
        list.add("Str" + Math.random() * 10);
        collectionReference = list;
    }

    public Object getCollectionReference() {
        return collectionReference;
    }

    public void setCollectionReference(Object collectionReference) {
        this.collectionReference = collectionReference;
    }
}
