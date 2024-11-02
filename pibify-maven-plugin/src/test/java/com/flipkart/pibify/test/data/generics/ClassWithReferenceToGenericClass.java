package com.flipkart.pibify.test.data.generics;

import com.flipkart.pibify.core.Pibify;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 02/11/24
 */
public class ClassWithReferenceToGenericClass {

    @Pibify(1)
    public AGenericClass<String> reference;

    @Pibify(2)
    public List<AGenericClass<String>> referenceList;

    public void randomize() {
        reference = new AGenericClass<>();
        reference.value = "str" + Math.random();

        referenceList = new ArrayList<>();
        referenceList.add(reference);
    }
}
