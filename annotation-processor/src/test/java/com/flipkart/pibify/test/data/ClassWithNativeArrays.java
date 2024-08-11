package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;

/**
 * This class is used for testing native fields handling by pibify
 * Author bageshwar.pn
 * Date 09/08/24
 */
public class ClassWithNativeArrays {

    @Pibify(1)
    private String[] aString = null;

    @Pibify(2)
    private int[] anInt = null;

    @Pibify(3)
    private boolean[] aBoolean = null;

    public ClassWithNativeArrays randomize() {
        aString = new String[]{"str" + Math.random(), "str" + Math.random(), "str" + Math.random()};
        anInt = new int[]{(int) (Math.random() * 1000), (int) (Math.random() * 1000), (int) (Math.random() * 1000)};
        aBoolean = new boolean[]{Math.random() > 0.5, Math.random() > 0.5, Math.random() > 0.5};
        return this;
    }

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
