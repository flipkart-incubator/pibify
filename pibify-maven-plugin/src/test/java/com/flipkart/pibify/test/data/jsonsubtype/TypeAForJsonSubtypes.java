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
public class TypeAForJsonSubtypes extends BaseClassForJsonSubType {

    @Pibify(1)
    private String typeAString;

    public TypeAForJsonSubtypes(String typeAString) {
        super(EnumB.A);
        this.typeAString = typeAString;
    }

    public static TypeAForJsonSubtypes randomize() {
        TypeAForJsonSubtypes subType = new TypeAForJsonSubtypes("str" + Math.random());
        subType.setAString("str" + Math.random());
        return subType;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TypeAForJsonSubtypes)) return false;
        if (!super.equals(o)) return false;
        TypeAForJsonSubtypes that = (TypeAForJsonSubtypes) o;
        return Objects.equals(typeAString, that.typeAString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), typeAString);
    }

    public String getTypeAString() {
        return typeAString;
    }

    public void setTypeAString(String typeAString) {
        this.typeAString = typeAString;
    }
}
