package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 23/09/24
 */
public class ClassWithMapReference {

    @Pibify(2)
    public Object treeMapReference;
    @Pibify(3)
    public Object linkedHashMapReference;
    @Pibify(1)
    Object hashMapReference;

    public void randomize() {
        HashMap<Object, Object> map = new HashMap<>();
        map.put(Math.random() * 10, "str" + Math.random() * 10);
        map.put(Math.random() * 10, "str" + Math.random() * 10);
        map.put(Math.random() * 10, "str" + Math.random() * 10);
        hashMapReference = map;

        treeMapReference = new TreeMap<>(map);
        linkedHashMapReference = new LinkedHashMap<>(map);
    }

    public Object getHashMapReference() {
        return hashMapReference;
    }

    public void setHashMapReference(Object hashMapReference) {
        this.hashMapReference = hashMapReference;
    }
}
