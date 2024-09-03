package com.flipkart.pibify.codegen;

import com.flipkart.pibify.test.data.ClassWithNestedCollections;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CodeGenUtilTest {


    @Test
    public void testNestedCollections() throws CodeGenException {
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithNestedCollections.class);
        String stringForField = CodeGenUtil.getGenericTypeStringForField(codeGenSpec.getFields().get(0));
        assertEquals("java.util.List<java.util.List<java.lang.String>>", stringForField);

        stringForField = CodeGenUtil.getGenericTypeStringForField(codeGenSpec.getFields().get(1));
        assertEquals("java.util.Set<java.util.Set<java.lang.Integer>>", stringForField);

        stringForField = CodeGenUtil.getGenericTypeStringForField(codeGenSpec.getFields().get(2));
        assertEquals("java.util.Set<java.util.List<java.lang.Integer>>", stringForField);

        stringForField = CodeGenUtil.getGenericTypeStringForField(codeGenSpec.getFields().get(3));
        assertEquals("java.util.List<java.util.Set<java.lang.Integer>>", stringForField);

        assertThrows(UnsupportedOperationException.class, () -> CodeGenUtil.getGenericTypeStringForField(codeGenSpec.getFields().get(4)));

        stringForField = CodeGenUtil.getGenericTypeStringForField(codeGenSpec.getFields().get(5));
        assertEquals("java.util.List<java.util.Set<java.lang.Integer>>", stringForField);
    }

}