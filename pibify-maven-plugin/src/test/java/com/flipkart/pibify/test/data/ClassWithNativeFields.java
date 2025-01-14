/*
 *
 *  *Copyright [2025] [Original Author]
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

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

    @Pibify(value = 12, dictionary = true)
    public String dString3;
    @Pibify(value = 10, dictionary = true)
    private String dString1;
    @Pibify(value = 11, dictionary = true)
    private String dString2;

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
        dString1 = "str" + Math.random();
        dString2 = dString1;
        dString3 = dString1;
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

    public String getdString1() {
        return dString1;
    }

    public void setdString1(String dString1) {
        this.dString1 = dString1;
    }

    public String getdString2() {
        return dString2;
    }

    public void setdString2(String dString2) {
        this.dString2 = dString2;
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
        dString1 = "";
        dString2 = "";
        dString3 = "";
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof ClassWithNativeFields)) return false;
        ClassWithNativeFields that = (ClassWithNativeFields) object;
        return anInt == that.anInt && aLong == that.aLong && Float.compare(aFloat, that.aFloat) == 0 && Double.compare(aDouble, that.aDouble) == 0 && aBoolean == that.aBoolean && aChar == that.aChar && aByte == that.aByte && aShort == that.aShort && Objects.equals(aString, that.aString) && Objects.equals(dString1, that.dString1) && Objects.equals(dString2, that.dString2) && Objects.equals(dString3, that.dString3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aString, anInt, aLong, aFloat, aDouble, aBoolean, aChar, aByte, aShort, dString1, dString2, dString3);
    }
}
