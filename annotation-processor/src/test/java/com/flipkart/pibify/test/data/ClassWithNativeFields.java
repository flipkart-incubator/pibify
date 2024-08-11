package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;

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

        if (anInt != that.anInt) return false;
        if (aLong != that.aLong) return false;
        if (Float.compare(that.aFloat, aFloat) != 0) return false;
        if (Double.compare(that.aDouble, aDouble) != 0) return false;
        if (aBoolean != that.aBoolean) return false;
        if (aChar != that.aChar) return false;
        if (aByte != that.aByte) return false;
        if (aShort != that.aShort) return false;
        return aString.equals(that.aString);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = aString.hashCode();
        result = 31 * result + anInt;
        result = 31 * result + (int) (aLong ^ (aLong >>> 32));
        result = 31 * result + (aFloat != +0.0f ? Float.floatToIntBits(aFloat) : 0);
        temp = Double.doubleToLongBits(aDouble);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (aBoolean ? 1 : 0);
        result = 31 * result + (int) aChar;
        result = 31 * result + (int) aByte;
        result = 31 * result + (int) aShort;
        return result;
    }
}
