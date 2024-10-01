package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;

import java.util.Objects;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 23/09/24
 */
public class ClassWithObjectReference {

    @Pibify(1)
    String str;

    @Pibify(2)
    Object stringReference;

    @Pibify(3)
    Object objectReference;

    @Pibify(4)
    private Object anInt;

    @Pibify(5)
    private Object aLong;

    @Pibify(6)
    private Object aFloat;

    @Pibify(7)
    private Object aDouble;

    @Pibify(8)
    private Object aBoolean;

    @Pibify(9)
    private Object aChar;

    @Pibify(10)
    private Object aByte;

    @Pibify(11)
    private Object aShort;

    public void randomize() {
        str = "str" + Math.random();
        stringReference = "str" + Math.random();
        ClassWithNativeFields classWithNativeFields = new ClassWithNativeFields();
        classWithNativeFields.randomize();
        objectReference = classWithNativeFields;

        anInt = (int) (Math.random() * 1000);
        aLong = (long) (Math.random() * 1000000);
        aFloat = (float) Math.random();
        aDouble = Math.random();
        aBoolean = Math.random() > 0.5;
        aChar = (char) (Math.random() * 100);
        aByte = (byte) (Math.random() * 100);
        aShort = (short) (Math.random() * 100);
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public Object getStringReference() {
        return stringReference;
    }

    public void setStringReference(Object stringReference) {
        this.stringReference = stringReference;
    }

    public Object getObjectReference() {
        return objectReference;
    }

    public void setObjectReference(Object objectReference) {
        this.objectReference = objectReference;
    }

    public Object getAnInt() {
        return anInt;
    }

    public void setAnInt(Object anInt) {
        this.anInt = anInt;
    }

    public Object getaLong() {
        return aLong;
    }

    public void setaLong(Object aLong) {
        this.aLong = aLong;
    }

    public Object getaFloat() {
        return aFloat;
    }

    public void setaFloat(Object aFloat) {
        this.aFloat = aFloat;
    }

    public Object getaDouble() {
        return aDouble;
    }

    public void setaDouble(Object aDouble) {
        this.aDouble = aDouble;
    }

    public Object getaBoolean() {
        return aBoolean;
    }

    public void setaBoolean(Object aBoolean) {
        this.aBoolean = aBoolean;
    }

    public Object getaChar() {
        return aChar;
    }

    public void setaChar(Object aChar) {
        this.aChar = aChar;
    }

    public Object getaByte() {
        return aByte;
    }

    public void setaByte(Object aByte) {
        this.aByte = aByte;
    }

    public Object getaShort() {
        return aShort;
    }

    public void setaShort(Object aShort) {
        this.aShort = aShort;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassWithObjectReference that = (ClassWithObjectReference) o;
        return Objects.equals(str, that.str) && Objects.equals(stringReference, that.stringReference) && Objects.equals(objectReference, that.objectReference) && Objects.equals(anInt, that.anInt) && Objects.equals(aLong, that.aLong) && Objects.equals(aFloat, that.aFloat) && Objects.equals(aDouble, that.aDouble) && Objects.equals(aBoolean, that.aBoolean) && Objects.equals(aChar, that.aChar) && Objects.equals(aByte, that.aByte) && Objects.equals(aShort, that.aShort);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(str);
        result = 31 * result + Objects.hashCode(stringReference);
        result = 31 * result + Objects.hashCode(objectReference);
        result = 31 * result + Objects.hashCode(anInt);
        result = 31 * result + Objects.hashCode(aLong);
        result = 31 * result + Objects.hashCode(aFloat);
        result = 31 * result + Objects.hashCode(aDouble);
        result = 31 * result + Objects.hashCode(aBoolean);
        result = 31 * result + Objects.hashCode(aChar);
        result = 31 * result + Objects.hashCode(aByte);
        result = 31 * result + Objects.hashCode(aShort);
        return result;
    }
}
