package com.flipkart.pibify.codegen;

import com.flipkart.pibify.test.data.ClassWithNestedCollections;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CodeGenUtilTest {


    @Test
    @Disabled
    public void testNestedCollections() throws CodeGenException {
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithNestedCollections.class);

        assertEquals("java.util.List<java.util.List<java.lang.String>>",
                codeGenSpec.getFields().get(0).getType().getGenericTypeSignature());
        assertEquals("java.util.List<java.lang.String>",
                codeGenSpec.getFields().get(0).getType().getContainerTypes().get(0).getGenericTypeSignature());
        assertEquals("java.lang.String",
                codeGenSpec.getFields().get(0).getType().getContainerTypes().get(0).getContainerTypes().get(0).getGenericTypeSignature());

        assertEquals("java.util.Set<java.util.Set<java.lang.Integer>>",
                codeGenSpec.getFields().get(1).getType().getGenericTypeSignature());
        assertEquals("java.util.Set<java.lang.Integer>",
                codeGenSpec.getFields().get(1).getType().getContainerTypes().get(0).getGenericTypeSignature());
        assertEquals("java.lang.Integer",
                codeGenSpec.getFields().get(1).getType().getContainerTypes().get(0).getContainerTypes().get(0).getGenericTypeSignature());

        assertEquals("java.util.Set<java.util.List<java.lang.Integer>>",
                codeGenSpec.getFields().get(2).getType().getGenericTypeSignature());
        assertEquals("java.util.List<java.lang.Integer>",
                codeGenSpec.getFields().get(2).getType().getContainerTypes().get(0).getGenericTypeSignature());
        assertEquals("java.lang.Integer",
                codeGenSpec.getFields().get(2).getType().getContainerTypes().get(0).getContainerTypes().get(0).getGenericTypeSignature());

        assertEquals("java.util.List<java.util.Set<java.lang.Integer>>",
                codeGenSpec.getFields().get(3).getType().getGenericTypeSignature());
        assertEquals("java.util.Set<java.lang.Integer>",
                codeGenSpec.getFields().get(3).getType().getContainerTypes().get(0).getGenericTypeSignature());
        assertEquals("java.lang.Integer",
                codeGenSpec.getFields().get(3).getType().getContainerTypes().get(0).getContainerTypes().get(0).getGenericTypeSignature());

        assertEquals("com.flipkart.pibify.test.data.ClassWithReferences",
                codeGenSpec.getFields().get(4).getType().getGenericTypeSignature());

        assertEquals("java.util.Map<java.lang.Float,java.util.Map<java.lang.String,java.lang.Boolean>>",
                codeGenSpec.getFields().get(5).getType().getGenericTypeSignature());

        assertEquals("java.util.Map<java.lang.Float,java.util.Map<java.lang.String,java.util.Map<java.lang.Integer,java.lang.Boolean>>>",
                codeGenSpec.getFields().get(6).getType().getGenericTypeSignature());
        assertEquals("java.lang.Float",
                codeGenSpec.getFields().get(6).getType().getContainerTypes().get(0).getGenericTypeSignature());
        assertEquals("java.util.Map<java.lang.String,java.util.Map<java.lang.Integer,java.lang.Boolean>>",
                codeGenSpec.getFields().get(6).getType().getContainerTypes().get(1).getGenericTypeSignature());
        assertEquals("java.lang.String",
                codeGenSpec.getFields().get(6).getType().getContainerTypes().get(1).getContainerTypes().get(0).getGenericTypeSignature());
        assertEquals("java.util.Map<java.lang.Integer,java.lang.Boolean>",
                codeGenSpec.getFields().get(6).getType().getContainerTypes().get(1).getContainerTypes().get(1).getGenericTypeSignature());
        assertEquals("java.lang.Integer",
                codeGenSpec.getFields().get(6).getType().getContainerTypes().get(1).getContainerTypes().get(1).getContainerTypes().get(0).getGenericTypeSignature());
        assertEquals("java.lang.Boolean",
                codeGenSpec.getFields().get(6).getType().getContainerTypes().get(1).getContainerTypes().get(1).getContainerTypes().get(1).getGenericTypeSignature());

        assertEquals("java.util.Map<java.util.Map<java.lang.Boolean,java.lang.String>,java.util.Map<java.lang.Integer,java.lang.Boolean>>",
                codeGenSpec.getFields().get(7).getType().getGenericTypeSignature());
        assertEquals("java.util.Map<java.lang.Boolean,java.lang.String>",
                codeGenSpec.getFields().get(7).getType().getContainerTypes().get(0).getGenericTypeSignature());
        assertEquals("java.lang.Boolean",
                codeGenSpec.getFields().get(7).getType().getContainerTypes().get(0).getContainerTypes().get(0).getGenericTypeSignature());
        assertEquals("java.lang.String",
                codeGenSpec.getFields().get(7).getType().getContainerTypes().get(0).getContainerTypes().get(1).getGenericTypeSignature());
        assertEquals("java.util.Map<java.lang.Integer,java.lang.Boolean>",
                codeGenSpec.getFields().get(7).getType().getContainerTypes().get(1).getGenericTypeSignature());
        assertEquals("java.lang.Integer",
                codeGenSpec.getFields().get(7).getType().getContainerTypes().get(1).getContainerTypes().get(0).getGenericTypeSignature());
        assertEquals("java.lang.Boolean",
                codeGenSpec.getFields().get(7).getType().getContainerTypes().get(1).getContainerTypes().get(1).getGenericTypeSignature());

        assertEquals("java.util.List<java.util.Map<java.lang.String,java.lang.Float>>",
                codeGenSpec.getFields().get(8).getType().getGenericTypeSignature());
        assertEquals("java.util.Map<java.lang.String,java.lang.Float>",
                codeGenSpec.getFields().get(8).getType().getContainerTypes().get(0).getGenericTypeSignature());
        assertEquals("java.lang.String",
                codeGenSpec.getFields().get(8).getType().getContainerTypes().get(0).getContainerTypes().get(0).getGenericTypeSignature());
        assertEquals("java.lang.Float",
                codeGenSpec.getFields().get(8).getType().getContainerTypes().get(0).getContainerTypes().get(1).getGenericTypeSignature());

        assertEquals("java.util.Set<java.util.Map<java.lang.String,java.lang.Float>>",
                codeGenSpec.getFields().get(9).getType().getGenericTypeSignature());
        assertEquals("java.util.Map<java.lang.String,java.lang.Float>",
                codeGenSpec.getFields().get(9).getType().getContainerTypes().get(0).getGenericTypeSignature());
        assertEquals("java.lang.String",
                codeGenSpec.getFields().get(9).getType().getContainerTypes().get(0).getContainerTypes().get(0).getGenericTypeSignature());
        assertEquals("java.lang.Float",
                codeGenSpec.getFields().get(9).getType().getContainerTypes().get(0).getContainerTypes().get(1).getGenericTypeSignature());

        assertEquals("java.util.Map<java.util.List<java.lang.String>,java.util.Set<java.lang.Boolean>>",
                codeGenSpec.getFields().get(10).getType().getGenericTypeSignature());
        assertEquals("java.util.List<java.lang.String>",
                codeGenSpec.getFields().get(10).getType().getContainerTypes().get(0).getGenericTypeSignature());
        assertEquals("java.lang.String",
                codeGenSpec.getFields().get(10).getType().getContainerTypes().get(0).getContainerTypes().get(0).getGenericTypeSignature());
        assertEquals("java.util.Set<java.lang.Boolean>",
                codeGenSpec.getFields().get(10).getType().getContainerTypes().get(1).getGenericTypeSignature());
        assertEquals("java.lang.Boolean",
                codeGenSpec.getFields().get(10).getType().getContainerTypes().get(1).getContainerTypes().get(0).getGenericTypeSignature());
    }

}