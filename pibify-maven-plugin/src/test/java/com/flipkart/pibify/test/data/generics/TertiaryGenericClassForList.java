package com.flipkart.pibify.test.data.generics;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 02/11/24
 */
public class TertiaryGenericClassForList<T> extends AGenericClass<List<T>> {

    public TertiaryGenericClassForList<T> randomize(T value1, T value2, T value3) {
        List<T> list = new ArrayList<>();
        list.add(value1);
        list.add(value2);
        list.add(value3);
        super.value = list;
        return this;
    }
}
