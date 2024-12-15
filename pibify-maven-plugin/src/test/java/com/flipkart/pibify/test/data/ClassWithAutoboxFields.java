package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;

import java.util.Objects;

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

    @Pibify(11)
    public Boolean isPublicBoolean;
    @Pibify(10)
    private Boolean isAnotherBoolean;

    public ClassWithAutoboxFields randomize() {

        anInt = (int) (Math.random() * 1000);
        aLong = (long) (Math.random() * 1000000);
        aFloat = (float) Math.random();
        aDouble = Math.random();
        aBoolean = Math.random() > 0.5;
        aChar = (char) (Math.random() * 100);
        aByte = (byte) (Math.random() * 100);
        aShort = (short) (Math.random() * 100);
        isAnotherBoolean = Math.random() > 0.5;
        isPublicBoolean = Math.random() > 0.5;
        return this;
    }

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

    public Boolean getAnotherBoolean() {
        return isAnotherBoolean;
    }

    public void setAnotherBoolean(Boolean anotherBoolean) {
        isAnotherBoolean = anotherBoolean;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ClassWithAutoboxFields)) return false;
        ClassWithAutoboxFields that = (ClassWithAutoboxFields) o;
        return Objects.equals(anInt, that.anInt) && Objects.equals(aLong, that.aLong) && Objects.equals(aFloat, that.aFloat) && Objects.equals(aDouble, that.aDouble) && Objects.equals(aBoolean, that.aBoolean) && Objects.equals(aChar, that.aChar) && Objects.equals(aByte, that.aByte) && Objects.equals(aShort, that.aShort) && Objects.equals(isAnotherBoolean, that.isAnotherBoolean) && Objects.equals(isPublicBoolean, that.isPublicBoolean);
    }

    @Override
    public int hashCode() {
        return Objects.hash(anInt, aLong, aFloat, aDouble, aBoolean, aChar, aByte, aShort, isAnotherBoolean, isPublicBoolean);
    }
}
