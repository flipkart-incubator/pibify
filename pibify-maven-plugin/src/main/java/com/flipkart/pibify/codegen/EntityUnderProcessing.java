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

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 30/09/24
 */
public class EntityUnderProcessing {
    private final Class<?> type;
    private final String fqdn;
    private Field reflectedField;

    public EntityUnderProcessing(Class<?> type) {
        this.type = type;
        this.fqdn = type.getCanonicalName();
    }

    public static EntityUnderProcessing of(Class<?> type) {
        return new EntityUnderProcessing(type);
    }

    public Field getReflectedField() {
        return reflectedField;
    }

    public void setReflectedField(Field reflectedField) {
        this.reflectedField = reflectedField;
    }

    public String getFqdn() {
        return fqdn;
    }

    public Class<?> getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntityUnderProcessing that = (EntityUnderProcessing) o;
        return Objects.equals(fqdn, that.fqdn);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(fqdn);
    }

    @Override
    public String toString() {
        return fqdn;
    }
}
