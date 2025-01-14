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

package com.flipkart.pibify.codegen;

/**
 * This class is used as a container for strings which can be used for case-insensitive maps without losing the actual string
 * Author bageshwar.pn
 * Date 30/09/24
 */
public class CaseInsensitiveString {
    private final String string;

    public CaseInsensitiveString(String string) {
        this.string = string;
    }

    public static CaseInsensitiveString of(String string) {
        return new CaseInsensitiveString(string);
    }

    public String getString() {
        return string;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CaseInsensitiveString that = (CaseInsensitiveString) o;
        return string.equalsIgnoreCase(that.string);
    }

    @Override
    public int hashCode() {
        return string.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return string;
    }
}
