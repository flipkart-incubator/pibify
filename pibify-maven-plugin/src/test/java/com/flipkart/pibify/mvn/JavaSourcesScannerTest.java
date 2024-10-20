package com.flipkart.pibify.mvn;

import com.flipkart.pibify.codegen.CodeGenUtil;
import com.flipkart.pibify.test.data.ClassWithNativeFields;
import com.flipkart.pibify.test.data.ClassWithNoFields;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JavaSourcesScannerTest {

    @Test
    public void testIsIgnorableClass() {
        assertTrue(CodeGenUtil.isNonPibifyClass(ClassWithNoFields.class));
        assertFalse(CodeGenUtil.isNonPibifyClass(ClassWithNativeFields.class));
        assertFalse(CodeGenUtil.isNonPibifyClass(ClassWithOnlySuperClassHavingPibify.class));
        assertTrue(CodeGenUtil.isNonPibifyClass(Serializable.class));
        assertTrue(CodeGenUtil.isNonPibifyClass(SerializableClass.class));
        assertFalse(CodeGenUtil.isNonPibifyClass(SerializableClassPibify.class));
        assertTrue(CodeGenUtil.isNonPibifyClass(BigDecimal.class));
    }
}