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
