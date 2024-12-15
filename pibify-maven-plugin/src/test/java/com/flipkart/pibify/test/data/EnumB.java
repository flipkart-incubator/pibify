package com.flipkart.pibify.test.data;

/**
 * This enum is used for
 * Author bageshwar.pn
 * Date 07/09/24
 */
public enum EnumB {
    A, B, C;

    public static EnumB randomize() {
        return EnumB.values()[(int) (Math.random() * EnumB.values().length)];
    }
}
