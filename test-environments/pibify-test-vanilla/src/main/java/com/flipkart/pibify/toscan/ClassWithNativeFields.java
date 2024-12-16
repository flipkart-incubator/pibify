package com.flipkart.pibify.toscan;

import com.flipkart.pibify.core.Pibify;

/**
 * This class is used for testing native fields handling by pibify
 * Author bageshwar.pn
 * Date 25/07/24
 */
public class ClassWithNativeFields {

    @Pibify(value = 1)
    private String aString = "2";

    @Pibify(value = 2)
    private int anInt = 2;

    private long aLong = 3L;

    private float aFloat = 4.0f;

    @Pibify(value = 3)
    private double aDouble = 5.0d;

    private boolean aBoolean = true;

    private char aChar = '7';

    private byte aByte = 8;

    private short aShort = 9;

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
}
