package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 07/09/24
 */
public class ClassHierarchy1 {

    @Pibify(3)
    public String str3;
    @Pibify(2)
    protected String str2;
    @Pibify(4)
    String str4;
    @Pibify(1)
    private String str1;

    public void randomize() {
        str1 = "str" + Math.random();
        str2 = "str" + Math.random();
        str3 = "str" + Math.random();
        str4 = "str" + Math.random();
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

    public String getStr3() {
        return str3;
    }

    public void setStr3(String str3) {
        this.str3 = str3;
    }

    public String getStr4() {
        return str4;
    }

    public void setStr4(String str4) {
        this.str4 = str4;
    }
}
