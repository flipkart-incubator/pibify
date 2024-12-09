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
