package com.flipkart.pibify.test.data.generics;

import com.flipkart.pibify.core.Pibify;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 02/11/24
 */
public class ATertiaryGenericClass<T> extends AnotherGenericClass<T> {

    @Pibify(1)
    public String str;

    @Override
    public void randomize(T value1, T value2, T value3) {
        super.randomize(value1, value2, value3);
        this.str = "str" + Math.random();
    }
}
