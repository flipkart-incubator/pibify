package com.flipkart.pibify.codegen;

import com.flipkart.pibify.test.data.ClassWithNativeFields;
import com.flipkart.pibify.test.util.SimpleCompiler;
import com.squareup.javapoet.JavaFile;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CodeGeneratorImplTest {

    private <T> T invokeGeneratedCode(JavaFile javaFile, T data) throws Exception {
        SimpleCompiler simpleCompiler = new SimpleCompiler();
        simpleCompiler.compile(javaFile.toJavaFileObject());
        Class<?> handlerClazz = simpleCompiler.loadClass("com.flipkart.pibify.generated." + data.getClass().getCanonicalName() + "Handler");
        Object handlerInstance = handlerClazz.newInstance();
        Method serialize = handlerClazz.getDeclaredMethod("serialize", ClassWithNativeFields.class);
        byte[] result = (byte[]) serialize.invoke(handlerInstance, data);

        Method deserialize = handlerClazz.getDeclaredMethod("deserialize", byte[].class);
        return (T) deserialize.invoke(handlerInstance, result);
    }

    @Test
    public void generateVanilla() throws Exception {

        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithNativeFields.class);

        CodeGeneratorImpl impl = new CodeGeneratorImpl();
        JavaFile javaFile = impl.generate(codeGenSpec);
        assertNotNull(javaFile);
        //javaFile.writeTo(System.out);
        ClassWithNativeFields testPayload = new ClassWithNativeFields();
        testPayload.randomize();
        ClassWithNativeFields deserialized = invokeGeneratedCode(javaFile, testPayload);

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