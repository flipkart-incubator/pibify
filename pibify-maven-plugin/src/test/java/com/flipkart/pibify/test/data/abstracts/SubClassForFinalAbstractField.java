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

package com.flipkart.pibify.test.data.abstracts;

import com.flipkart.pibify.core.Pibify;

import java.util.Objects;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 09/12/24
 */
public class SubClassForFinalAbstractField extends BaseClass {

    @Pibify(1)
    private String subStr;

    public SubClassForFinalAbstractField() {
        super("str");
    }

    public static SubClassForFinalAbstractField randomize() {
        SubClassForFinalAbstractField subClass = new SubClassForFinalAbstractField();
        subClass.setSubStr("str");
        return subClass;
    }

    public String getSubStr() {
        return subStr;
    }

    public void setSubStr(String subStr) {
        this.subStr = subStr;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SubClassForFinalAbstractField)) return false;
        if (!super.equals(o)) return false;
        SubClassForFinalAbstractField subClass = (SubClassForFinalAbstractField) o;
        return Objects.equals(subStr, subClass.subStr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subStr);
    }
}
