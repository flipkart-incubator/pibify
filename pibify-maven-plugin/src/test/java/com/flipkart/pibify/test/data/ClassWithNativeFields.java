package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;

import java.util.Objects;

/**
 * This class is used for testing native fields handling by pibify
 * Author bageshwar.pn
 * Date 25/07/24
 */
public class ClassWithNativeFields {

    @Pibify(1)
    private String aString = "1";

    @Pibify(2)
    private int anInt = 2;

    @Pibify(3)
    private long aLong = 3L;

    @Pibify(4)
    private float aFloat = 4.0f;

    @Pibify(5)
    private double aDouble = 5.0d;

    @Pibify(6)
    private boolean aBoolean = true;

    @Pibify(7)
    private char aChar = '7';

    @Pibify(8)
    private byte aByte = 8;

    @Pibify(9)
    private short aShort = 9;

    public ClassWithNativeFields randomize() {
        aString = "str" + Math.random();
        anInt = (int) (Math.random() * 1000);
        aLong = (long) (Math.random() * 1000000);
        aFloat = (float) Math.random();
        aDouble = Math.random();
        aBoolean = Math.random() > 0.5;
        aChar = (char) (Math.random() * 100);
        aByte = (byte) (Math.random() * 100);
        aShort = (short) (Math.random() * 100);
        return this;
    }

    public String getaString() {
        return aString;
    }

    public void setaString(String aString) {
        this.aString = aString;
    }

    public int getAnInt() {
        return anInt;
    }

    public void setAnInt(int anInt) {
        this.anInt = anInt;
    }

    public long getaLong() {
        return aLong;
    }

    public void setaLong(long aLong) {
        this.aLong = aLong;
    }

    public float getaFloat() {
        return aFloat;
    }

    public void setaFloat(float aFloat) {
        this.aFloat = aFloat;
    }

    public double getaDouble() {
        return aDouble;
    }

    public void setaDouble(double aDouble) {
        this.aDouble = aDouble;
    }

    public boolean isaBoolean() {
        return aBoolean;
    }

    public void setaBoolean(boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    public char getaChar() {
        return aChar;
    }

    public void setaChar(char aChar) {
        this.aChar = aChar;
    }

    public byte getaByte() {
        return aByte;
    }

    public void setaByte(byte aByte) {
        this.aByte = aByte;
    }

    public short getaShort() {
        return aShort;
    }

    public void setaShort(short aShort) {
        this.aShort = aShort;
    }

    public void resetAll() {
        aString = "";
        anInt = -1;
        aFloat = -1;
        aDouble = -1;
        aChar = '0';
        aBoolean = false;
        aLong = -1;
        aShort = -1;
        aByte = -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassWithNativeFields that = (ClassWithNativeFields) o;
        return anInt == that.anInt && aLong == that.aLong && Float.compare(aFloat, that.aFloat) == 0 && Double.compare(aDouble, that.aDouble) == 0 && aBoolean == that.aBoolean && aChar == that.aChar && aByte == that.aByte && aShort == that.aShort && Objects.equals(aString, that.aString);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(aString);
        result = 31 * result + anInt;
        result = 31 * result + Long.hashCode(aLong);
        result = 31 * result + Float.hashCode(aFloat);
        result = 31 * result + Double.hashCode(aDouble);
        result = 31 * result + Boolean.hashCode(aBoolean);
        result = 31 * result + aChar;
        result = 31 * result + aByte;
        result = 31 * result + aShort;
        return result;
    }
}
