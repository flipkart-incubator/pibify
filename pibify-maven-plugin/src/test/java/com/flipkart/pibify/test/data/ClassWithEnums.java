package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 07/09/24
 */
public class ClassWithEnums {

    @Pibify(1)
    private EnumA enumA;
    @Pibify(2)
    private EnumB enumB;
    @Pibify(3)
    private EnumB nullEnum = null;

    public void randomize() {
        enumA = EnumA.values()[((int) (Math.random() * 10)) % 3];
        enumB = EnumB.values()[((int) (Math.random() * 10)) % 3];
    }

    public EnumA getEnumA() {
        return enumA;
    }

    public void setEnumA(EnumA enumA) {
        this.enumA = enumA;
    }

    public EnumB getEnumB() {
        return enumB;
    }

    public void setEnumB(EnumB enumB) {
        this.enumB = enumB;
    }

    public EnumB getNullEnum() {
        return nullEnum;
    }

    public void setNullEnum(EnumB nullEnum) {
        this.nullEnum = nullEnum;
    }

    public enum EnumA {
        A, B, C
    }
}
