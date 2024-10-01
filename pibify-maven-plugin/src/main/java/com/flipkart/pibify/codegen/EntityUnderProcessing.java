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

    public EntityUnderProcessing(Class<?> type, String fqdn) {
        this.type = type;
        this.fqdn = fqdn;
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
