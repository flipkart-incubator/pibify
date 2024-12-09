package com.flipkart.pibify.test.data.abstracts;

import com.flipkart.pibify.core.Pibify;

import java.util.Objects;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 09/12/24
 */
public abstract class BaseClass {

    @Pibify(1)
    private final String str;

    public BaseClass(String str) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BaseClass)) return false;
        BaseClass baseClass = (BaseClass) o;
        return Objects.equals(str, baseClass.str);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(str);
    }
}
