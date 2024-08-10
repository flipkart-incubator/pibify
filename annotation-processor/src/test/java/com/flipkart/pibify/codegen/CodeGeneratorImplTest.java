package com.flipkart.pibify.codegen;

import com.flipkart.pibify.test.data.ClassWithNativeFields;
import com.flipkart.pibify.test.util.SimpleCompiler;
import com.squareup.javapoet.JavaFile;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CodeGeneratorImplTest {
    @Test
    public void generateVanilla() throws Exception {

        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithNativeFields.class);

        CodeGeneratorImpl impl = new CodeGeneratorImpl();
        JavaFile javaFile = impl.generate(codeGenSpec);
        assertNotNull(javaFile);
        //javaFile.writeTo(System.out);

        SimpleCompiler simpleCompiler = new SimpleCompiler();
        simpleCompiler.compile(javaFile.toJavaFileObject());
        Class<?> handlerClazz = simpleCompiler.loadClass("com.flipkart.pibify.generated.com.flipkart.pibify.codegen.ClassWithNativeFieldsHandler");
        Object handlerInstance = handlerClazz.newInstance();
        ClassWithNativeFields testPayload = new ClassWithNativeFields();
        testPayload.randomize();
        Method serialize = handlerClazz.getDeclaredMethod("serialize", ClassWithNativeFields.class);
        byte[] result = (byte[]) serialize.invoke(handlerInstance, testPayload);

        Method deserialize = handlerClazz.getDeclaredMethod("deserialize", byte[].class);
        ClassWithNativeFields deserialized = (ClassWithNativeFields) deserialize.invoke(handlerInstance, result);

        assertEquals(testPayload.getaByte(), deserialized.getaByte());
        assertEquals(testPayload.getaChar(), deserialized.getaChar());
        assertEquals(testPayload.getaDouble(), deserialized.getaDouble());
        assertEquals(testPayload.getaFloat(), deserialized.getaFloat());
        assertEquals(testPayload.getaLong(), deserialized.getaLong());
        assertEquals(testPayload.getAnInt(), deserialized.getAnInt());
        assertEquals(testPayload.getaShort(), deserialized.getaShort());
        assertEquals(testPayload.getaString(), deserialized.getaString());
        assertEquals(testPayload.isaBoolean(), deserialized.isaBoolean());
    }
}