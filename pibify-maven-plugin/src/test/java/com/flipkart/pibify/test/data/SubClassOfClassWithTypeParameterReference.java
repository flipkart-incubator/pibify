package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 20/09/24
 */
public class SubClassOfClassWithTypeParameterReference
        extends ClassWithTypeParameterReference<ClassWithReferences> {

    @Pibify(1)
    String str;

    public void randomize() {
        str = "str" + Math.random();
        ClassWithReferences references = new ClassWithReferences();
        setGenericTypeReference(references.randomize());

        list = new ArrayList<>();
        list.add(references);

        list2 = new ArrayList<>();
        list2.add(list);

        map = new HashMap<>();
        map.put(references, references);

        map2 = new HashMap<>();
        HashSet<ClassWithReferences> objects = new HashSet<>();
        objects.add(references);
        map2.put(list, objects);
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubClassOfClassWithTypeParameterReference that = (SubClassOfClassWithTypeParameterReference) o;
        return Objects.equals(str, that.str) && super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(str);
    }
}
