package com.flipkart.pibify.codegen;

import com.flipkart.pibify.test.data.ClassWithNestedCollections;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CodeGenUtilTest {


    @Test
    public void testNestedCollections() throws CodeGenException {
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        String stringForField;
        CodeGenSpec codeGenSpec = creator.create(ClassWithNestedCollections.class);
        stringForField = CodeGenUtil.getGenericTypeStringForField(codeGenSpec.getFields().get(0));
        assertEquals("java.util.List<java.util.List<java.lang.String>>", stringForField);

        stringForField = CodeGenUtil.getGenericTypeStringForField(codeGenSpec.getFields().get(1));
        assertEquals("java.util.Set<java.util.Set<java.lang.Integer>>", stringForField);

        stringForField = CodeGenUtil.getGenericTypeStringForField(codeGenSpec.getFields().get(2));
        assertEquals("java.util.Set<java.util.List<java.lang.Integer>>", stringForField);

        stringForField = CodeGenUtil.getGenericTypeStringForField(codeGenSpec.getFields().get(3));
        assertEquals("java.util.List<java.util.Set<java.lang.Integer>>", stringForField);

        assertThrows(UnsupportedOperationException.class, () -> CodeGenUtil.getGenericTypeStringForField(codeGenSpec.getFields().get(4)));

        stringForField = CodeGenUtil.getGenericTypeStringForField(codeGenSpec.getFields().get(5));
        assertEquals("java.util.Map<java.lang.Float,java.util.Map<java.lang.String,java.lang.Boolean>>", stringForField);

        stringForField = CodeGenUtil.getGenericTypeStringForField(codeGenSpec.getFields().get(6));
        assertEquals("java.util.Map<java.lang.Float,java.util.Map<java.lang.String,java.util.Map<java.lang.Integer,java.lang.Boolean>>>", stringForField);

        stringForField = CodeGenUtil.getGenericTypeStringForField(codeGenSpec.getFields().get(7));
        assertEquals("java.util.Map<java.util.Map<java.lang.Boolean,java.lang.String>,java.util.Map<java.lang.Integer,java.lang.Boolean>>", stringForField);

        stringForField = CodeGenUtil.getGenericTypeStringForField(codeGenSpec.getFields().get(8));
        assertEquals("java.util.List<java.util.Map<java.lang.String,java.lang.Float>>", stringForField);

        stringForField = CodeGenUtil.getGenericTypeStringForField(codeGenSpec.getFields().get(9));
        assertEquals("java.util.Set<java.util.Map<java.lang.String,java.lang.Float>>", stringForField);

        stringForField = CodeGenUtil.getGenericTypeStringForField(codeGenSpec.getFields().get(10));
        assertEquals("java.util.Map<java.util.List<java.lang.String>,java.util.Set<java.lang.Boolean>>", stringForField);
    }

}