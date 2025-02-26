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
import com.flipkart.pibify.test.data.another.AnotherClassWithNativeCollections;

import java.util.Date;
import java.util.Objects;

/**
 * This class is used for testing Class Refs
 * Author bageshwar.pn
 * Date 10/08/24
 */
public class ClassWithReferences {

    @Pibify(1)
    private AnotherClassWithNativeCollections reference;

    @Pibify(2)
    private String aString;

    @Pibify(3)
    private Date date;

    public ClassWithReferences randomize() {
        aString = "str" + Math.random();
        date = new Date(System.currentTimeMillis());
        reference = new AnotherClassWithNativeCollections();
        reference.randomize();
        return this;
    }

    public AnotherClassWithNativeCollections getReference() {
        return reference;
    }

    public void setReference(AnotherClassWithNativeCollections reference) {
        this.reference = reference;
    }

    public String getaString() {
        return aString;
    }

    public void setaString(String aString) {
        this.aString = aString;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ClassWithReferences)) return false;
        ClassWithReferences that = (ClassWithReferences) o;
        return Objects.equals(reference, that.reference) && Objects.equals(aString, that.aString) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reference, aString, date);
    }
}
