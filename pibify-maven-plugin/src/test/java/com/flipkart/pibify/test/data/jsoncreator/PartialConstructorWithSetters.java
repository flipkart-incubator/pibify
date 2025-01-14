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

package com.flipkart.pibify.test.data.jsoncreator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flipkart.pibify.core.Pibify;
import com.flipkart.pibify.test.data.ClassWithNativeFields;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 08/12/24
 */
public class PartialConstructorWithSetters {

    @Pibify(1)
    private String aString;

    @Pibify(2)
    private String aString2;

    @Pibify(3)
    private Double aDouble;

    @Pibify(8)
    public String publicString;
    @Pibify(9)
    public byte publicByte;
    @Pibify(10)
    public int publicInt;
    @Pibify(4)
    private BigDecimal aBigDecimal;
    @Pibify(5)
    private List<String> aList;
    @Pibify(6)
    private String[] aArray;
    @Pibify(7)
    private ClassWithNativeFields reference;

    @JsonCreator
    public PartialConstructorWithSetters(@JsonProperty("aString") String aString,
                                         @JsonProperty("aDouble") Double aDouble) {
        this.aString = aString;
        this.aDouble = aDouble;
    }

    public static PartialConstructorWithSetters randomize() {
        PartialConstructorWithSetters object = new PartialConstructorWithSetters(
                "str" + Math.random(),
                Math.random()
        );

        object.setaString2("str" + Math.random());
        object.setaBigDecimal(BigDecimal.valueOf(Math.random()));
        object.setaList(Arrays.asList("str" + Math.random(), "str" + Math.random()));
        object.aArray = new String[]{"str" + Math.random(), "str" + Math.random()};
        object.reference = new ClassWithNativeFields().randomize();
        object.publicString = "str" + Math.random();
        object.publicByte = (byte) (Math.random() * 100);
        object.publicInt = (int) (Math.random() * 1000);
        return object;
    }

    public String getaString() {
        return aString;
    }

    public String getaString2() {
        return aString2;
    }

    public void setaString2(String aString2) {
        this.aString2 = aString2;
    }

    public Double getaDouble() {
        return aDouble;
    }

    public List<String> getaList() {
        return aList;
    }

    public void setaList(List<String> aList) {
        this.aList = aList;
    }

    public BigDecimal getaBigDecimal() {
        return aBigDecimal;
    }

    public void setaBigDecimal(BigDecimal aBigDecimal) {
        this.aBigDecimal = aBigDecimal;
    }

    public String[] getaArray() {
        return aArray;
    }

    public void setaArray(String[] aArray) {
        this.aArray = aArray;
    }

    public ClassWithNativeFields getReference() {
        return reference;
    }

    public void setReference(ClassWithNativeFields reference) {
        this.reference = reference;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PartialConstructorWithSetters)) return false;
        PartialConstructorWithSetters that = (PartialConstructorWithSetters) o;
        return publicByte == that.publicByte && publicInt == that.publicInt && Objects.equals(aString, that.aString) && Objects.equals(aString2, that.aString2) && Objects.equals(aDouble, that.aDouble) && Objects.equals(aBigDecimal, that.aBigDecimal) && Objects.equals(aList, that.aList) && Objects.deepEquals(aArray, that.aArray) && Objects.equals(reference, that.reference) && Objects.equals(publicString, that.publicString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aString, aString2, aDouble, aBigDecimal, aList, Arrays.hashCode(aArray), reference, publicString, publicByte, publicInt);
    }
}
