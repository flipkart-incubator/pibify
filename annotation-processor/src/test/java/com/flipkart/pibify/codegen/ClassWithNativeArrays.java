package com.flipkart.pibify.codegen;

import com.flipkart.pibify.core.Pibify;

/**
 * This class is used for testing native fields handling by pibify
 * Author bageshwar.pn
 * Date 09/08/24
 */
public class ClassWithNativeArrays {

    @Pibify(1)
    private String[] aString = new String[]{"1"};

    @Pibify(2)
    private int[] anInt = new int[]{2};

    @Pibify(3)
    private boolean[] aBoolean = new boolean[]{true};


    public String[] getaString() {
        return aString;
    }

    public void setaString(String[] aString) {
        this.aString = aString;
    }

    public int[] getAnInt() {
        return anInt;
    }

    public void setAnInt(int[] anInt) {
        this.anInt = anInt;
    }

    public boolean[] getaBoolean() {
        return aBoolean;
    }

    public void setaBoolean(boolean[] aBoolean) {
        this.aBoolean = aBoolean;
    }
}
