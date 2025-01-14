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

    //@Pibify(value = 2)
    @Pibify(value = 4)
    private int anInt = 2;

    @Pibify(value = 5)
    private long aLong = 3L;

    @Pibify(value = 6)
    private float aFloat = 4.0f;

    @Pibify(value = 3)
    private double aDouble = 5.0d;

    @Pibify(value = 7)
    private boolean aBoolean = true;

    @Pibify(value = 8)
    private char aChar = '7';

    @Pibify(value = 9)
    private byte aByte = 8;

    @Pibify(value = 10)
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
