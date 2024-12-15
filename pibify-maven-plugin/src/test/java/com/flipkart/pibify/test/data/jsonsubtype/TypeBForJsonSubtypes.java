package com.flipkart.pibify.test.data.jsonsubtype;

import com.flipkart.pibify.core.Pibify;
import com.flipkart.pibify.test.data.EnumB;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 15/12/24
 */
@NoArgsConstructor
public class TypeBForJsonSubtypes extends BaseClassForJsonSubType {

    @Pibify(2)
    private String typeBString;

    public TypeBForJsonSubtypes(String typeBString) {
        super(EnumB.B);
        this.typeBString = typeBString;
    }

    public static TypeBForJsonSubtypes randomize() {
        TypeBForJsonSubtypes subType = new TypeBForJsonSubtypes("str" + Math.random());
        subType.setAString("str" + Math.random());
        return subType;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TypeBForJsonSubtypes)) return false;
        if (!super.equals(o)) return false;
        TypeBForJsonSubtypes that = (TypeBForJsonSubtypes) o;
        return Objects.equals(typeBString, that.typeBString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), typeBString);
    }

    public String getTypeBString() {
        return typeBString;
    }

    public void setTypeBString(String typeBString) {
        this.typeBString = typeBString;
    }
}
