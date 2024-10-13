package com.flipkart.pibify.test.data.generics;

import com.flipkart.pibify.core.Pibify;

import java.util.ArrayList;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 12/10/24
 */
public class ListClassLevel2<T> extends ArrayList<String> {

    @Pibify(1)
    private String str;

    @Pibify(2)
    private T obj;

    public void randomize(T key) {
        str = "str" + Math.random();
        obj = key;
        this.add("str" + Math.random());
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
