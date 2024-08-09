package com.flipkart.pibify.codegen;

import com.flipkart.pibify.core.Pibify;

/**
 * This class is used for testing native fields handling by pibify
 * Author bageshwar.pn
 * Date 09/08/24
 */
public class ClassWithAutoboxFields {

    @Pibify(2)
    private Integer anInt = 2;

    @Pibify(3)
    private Long aLong = 3L;

    @Pibify(4)
    private Float aFloat = 4.0f;

    @Pibify(5)
    private Double aDouble = 5.0d;

    @Pibify(6)
    private Boolean aBoolean = true;

    @Pibify(7)
    private Character aChar = '7';

    @Pibify(8)
    private Byte aByte = 8;

    @Pibify(9)
    private Short aShort = 9;

    public Integer getAnInt() {
        return anInt;
    }

    public void setAnInt(Integer anInt) {
        this.anInt = anInt;
    }

    public Long getaLong() {
        return aLong;
    }

    public void setaLong(Long aLong) {
        this.aLong = aLong;
    }

    public Float getaFloat() {
        return aFloat;
    }

    public void setaFloat(Float aFloat) {
        this.aFloat = aFloat;
    }

    public Double getaDouble() {
        return aDouble;
    }

    public void setaDouble(Double aDouble) {
        this.aDouble = aDouble;
    }

    public Boolean getaBoolean() {
        return aBoolean;
    }

    public void setaBoolean(Boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    public Character getaChar() {
        return aChar;
    }

    public void setaChar(Character aChar) {
        this.aChar = aChar;
    }

    public Byte getaByte() {
        return aByte;
    }

    public void setaByte(Byte aByte) {
        this.aByte = aByte;
    }

    public Short getaShort() {
        return aShort;
    }

    public void setaShort(Short aShort) {
        this.aShort = aShort;
    }
}
