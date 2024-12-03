package com.flipkart.pibify.core;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a member field with its index and other proto decorations
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Pibify {
    int value();

    boolean nullable() default true;

    /**
     * This flag, when true, causes the serializer to put the field in a dictionary
     * and reference it back during deserialization.
     * This is useful when the field is repeated and has a lot of repeated values.
     * <p>
     * The flag is only supported for Strings at the moment
     *
     * @return dictionary
     */
    boolean dictionary() default false;
}
