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

package com.flipkart.pibify;

import com.flipkart.pibify.core.Pibify;

/**
 * This class is used for testing native fields handling by pibify
 * Author bageshwar.pn
 * Date 09/08/24
 */
public class ClassWithAutoboxFields {

    @Pibify(value = 2)
    private Integer anInt = 2;

    @Pibify(value = 3)
    private Long aLong = 3L;

    @Pibify(value = 4)
    private Float aFloat = 4.0f;

    @Pibify(value = 5)
    private Double aDouble = 5.0d;

    @Pibify(value = 6)
    private Boolean aBoolean = true;

    @Pibify(value = 7)
    private Character aChar = '7';

    @Pibify(value = 8)
    private Byte aByte = 8;

    @Pibify(value = 9)
    private Short aShort = 9;

    public ClassWithAutoboxFields randomize() {

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
