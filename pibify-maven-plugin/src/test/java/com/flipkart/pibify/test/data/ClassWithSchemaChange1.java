package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 23/09/24
 */
public class ClassWithSchemaChange1 {

    @Pibify(1)
    String str1;
    @Pibify(2)
    String str2;

    public void randomize() {
        str1 = "str" + Math.random();
        str2 = "str2" + Math.random();
    }

    public String getStr1() {
        return str1;
    }

    public void setStr1(String str1) {
        this.str1 = str1;
    }

    public String getStr2() {
        return str2;
    }

    public void setStr2(String str2) {
        this.str2 = str2;
    }
}
