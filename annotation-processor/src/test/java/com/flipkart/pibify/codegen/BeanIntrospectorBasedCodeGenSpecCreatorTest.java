package com.flipkart.pibify.codegen;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BeanIntrospectorBasedCodeGenSpecCreatorTest {

    @Test
    void create() throws CodeGenException {
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithNativeFields.class);
        assertNotNull(codeGenSpec);
        assertEquals(9, codeGenSpec.getFields().size());

        Map<String, CodeGenSpec.FieldSpec> nameToFields = codeGenSpec.getFields().stream()
                .collect(Collectors.toMap(CodeGenSpec.FieldSpec::getName, f -> f));

        CodeGenSpec.FieldSpec field = nameToFields.get("aString");
        assertEquals("aString", field.getName());
        assertEquals("getaString", field.getGetter());
        assertEquals("setaString", field.getSetter());
        assertEquals(1, field.getIndex());
        assertEquals(CodeGenSpec.DataType.STRING, field.getType().nativeType);


        field = nameToFields.get("anInt");
        assertEquals("anInt", field.getName());
        assertEquals("getAnInt", field.getGetter());
        assertEquals("setAnInt", field.getSetter());
        assertEquals(2, field.getIndex());
        assertEquals(CodeGenSpec.DataType.INT, field.getType().nativeType);

        field = nameToFields.get("aLong");
        assertEquals("aLong", field.getName());
        assertEquals("getaLong", field.getGetter());
        assertEquals("setaLong", field.getSetter());
        assertEquals(3, field.getIndex());
        assertEquals(CodeGenSpec.DataType.LONG, field.getType().nativeType);

        field = nameToFields.get("aFloat");
        assertEquals("aFloat", field.getName());
        assertEquals("getaFloat", field.getGetter());
        assertEquals("setaFloat", field.getSetter());
        assertEquals(4, field.getIndex());
        assertEquals(CodeGenSpec.DataType.FLOAT, field.getType().nativeType);

        field = nameToFields.get("aDouble");
        assertEquals("aDouble", field.getName());
        assertEquals("getaDouble", field.getGetter());
        assertEquals("setaDouble", field.getSetter());
        assertEquals(5, field.getIndex());
        assertEquals(CodeGenSpec.DataType.DOUBLE, field.getType().nativeType);

        field = nameToFields.get("aDouble");
        assertEquals("aDouble", field.getName());
        assertEquals("getaDouble", field.getGetter());
        assertEquals("setaDouble", field.getSetter());
        assertEquals(5, field.getIndex());
        assertEquals(CodeGenSpec.DataType.DOUBLE, field.getType().nativeType);

        field = nameToFields.get("aBoolean");
        assertEquals("aBoolean", field.getName());
        assertEquals("isaBoolean", field.getGetter());
        assertEquals("setaBoolean", field.getSetter());
        assertEquals(6, field.getIndex());
        assertEquals(CodeGenSpec.DataType.BOOLEAN, field.getType().nativeType);

        field = nameToFields.get("aChar");
        assertEquals("aChar", field.getName());
        assertEquals("getaChar", field.getGetter());
        assertEquals("setaChar", field.getSetter());
        assertEquals(7, field.getIndex());
        assertEquals(CodeGenSpec.DataType.CHAR, field.getType().nativeType);

        field = nameToFields.get("aByte");
        assertEquals("aByte", field.getName());
        assertEquals("getaByte", field.getGetter());
        assertEquals("setaByte", field.getSetter());
        assertEquals(8, field.getIndex());
        assertEquals(CodeGenSpec.DataType.BYTE, field.getType().nativeType);

        field = nameToFields.get("aShort");
        assertEquals("aShort", field.getName());
        assertEquals("getaShort", field.getGetter());
        assertEquals("setaShort", field.getSetter());
        assertEquals(9, field.getIndex());
        assertEquals(CodeGenSpec.DataType.SHORT, field.getType().nativeType);
    }
}