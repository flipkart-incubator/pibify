package com.flipkart.pibify.mvn;

import com.flipkart.pibify.test.data.ClassWithNativeFields;
import com.flipkart.pibify.test.data.ClassWithNoFields;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JavaSourcesScannerTest {

    @Test
    public void testIsIgnorableClass() {
        assertTrue(JavaSourcesScanner.isIgnorableClass(ClassWithNoFields.class));
        assertFalse(JavaSourcesScanner.isIgnorableClass(ClassWithNativeFields.class));
        assertFalse(JavaSourcesScanner.isIgnorableClass(ClassWithOnlySuperClassHavingPibify.class));
        assertTrue(JavaSourcesScanner.isIgnorableClass(Serializable.class));
        assertTrue(JavaSourcesScanner.isIgnorableClass(SerializableClass.class));
        assertFalse(JavaSourcesScanner.isIgnorableClass(SerializableClassPibify.class));
    }
}