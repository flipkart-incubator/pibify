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

    /**
     * Randomizes the current instance of ClassWithReferences by setting its properties to random or current values.
     *
     * @return the current instance with randomized properties
     * @see AnotherClassWithNativeCollections#randomize()
     */
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

    /**
     * Sets the string value for this instance.
     *
     * @param aString the string to be assigned to the {@code aString} field
     */
    public void setaString(String aString) {
        this.aString = aString;
    }

    /**
     * Retrieves the current date associated with this instance.
     *
     * @return the Date object representing the date of this instance
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the date for this instance of ClassWithReferences.
     *
     * @param date the Date object to be assigned to this instance's date field
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Compares this {@code ClassWithReferences} instance with another object for equality.
     *
     * @param o the object to compare with this instance
     * @return {@code true} if the objects are equal, {@code false} otherwise
     *
     * Two {@code ClassWithReferences} instances are considered equal if they have:
     * - The same reference object
     * - The same string value
     * - The same date
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ClassWithReferences)) return false;
        ClassWithReferences that = (ClassWithReferences) o;
        return Objects.equals(reference, that.reference) && Objects.equals(aString, that.aString) && Objects.equals(date, that.date);
    }

    /**
     * Generates a hash code for the current instance based on its reference, string, and date fields.
     *
     * @return an integer hash code that combines the hash values of the reference, aString, and date fields
     */
    @Override
    public int hashCode() {
        return Objects.hash(reference, aString, date);
    }
}
