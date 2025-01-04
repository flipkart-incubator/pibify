package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;

import java.util.Objects;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 04/01/25
 */
public class ClassWithSelfReference {

    @Pibify(1)
    public String str3;

    @Pibify(2)
    public ClassWithSelfReference self;

    @Pibify(3)
    public Object objectReference;

    public static ClassWithSelfReference randomize() {
        ClassWithSelfReference classWithSelfReference = new ClassWithSelfReference();
        classWithSelfReference.str3 = "str" + Math.random();
        classWithSelfReference.self = null;
        classWithSelfReference.objectReference = "str" + Math.random();

        return classWithSelfReference;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ClassWithSelfReference)) return false;
        ClassWithSelfReference that = (ClassWithSelfReference) o;
        return Objects.equals(str3, that.str3) && Objects.equals(self, that.self) && Objects.equals(objectReference, that.objectReference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(str3, self, objectReference);
    }
}
