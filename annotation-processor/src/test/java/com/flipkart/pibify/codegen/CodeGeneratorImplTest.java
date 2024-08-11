package com.flipkart.pibify.codegen;

import com.flipkart.pibify.test.data.ClassWithAutoboxFields;
import com.flipkart.pibify.test.data.ClassWithNativeArrays;
import com.flipkart.pibify.test.data.ClassWithNativeCollections;
import com.flipkart.pibify.test.data.ClassWithNativeFields;
import com.flipkart.pibify.test.util.SimpleCompiler;
import com.squareup.javapoet.JavaFile;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SuppressWarnings("all")
class CodeGeneratorImplTest {

    private <T> T invokeGeneratedCode(JavaFile javaFile, T data) throws Exception {
        SimpleCompiler simpleCompiler = new SimpleCompiler();
        simpleCompiler.compile(javaFile.toJavaFileObject());
        Class<?> handlerClazz = simpleCompiler.loadClass("com.flipkart.pibify.generated." + data.getClass().getCanonicalName() + "Handler");
        Object handlerInstance = handlerClazz.newInstance();
        Method serialize = handlerClazz.getDeclaredMethod("serialize", data.getClass());
        byte[] result = (byte[]) serialize.invoke(handlerInstance, data);

        Method deserialize = handlerClazz.getDeclaredMethod("deserialize", byte[].class);
        return (T) deserialize.invoke(handlerInstance, result);
    }

    @Test
    public void testClassWithNativeFields() throws Exception {

        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithNativeFields.class);

        ICodeGenerator impl = new CodeGeneratorImpl();
        JavaFileWrapper javaFile = impl.generate(codeGenSpec);
        assertNotNull(javaFile.getJavaFile());
        //javaFile.writeTo(System.out);
        ClassWithNativeFields testPayload = new ClassWithNativeFields();
        testPayload.randomize();
        ClassWithNativeFields deserialized = invokeGeneratedCode(javaFile.getJavaFile(), testPayload);

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

    @Test
    public void testClassWithAutoboxFields() throws Exception {

        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithAutoboxFields.class);

        ICodeGenerator impl = new CodeGeneratorImpl();
        JavaFileWrapper javaFile = impl.generate(codeGenSpec);
        assertNotNull(javaFile.getJavaFile());
        //javaFile.writeTo(System.out);
        ClassWithAutoboxFields testPayload = new ClassWithAutoboxFields();
        testPayload.randomize();
        ClassWithAutoboxFields deserialized = invokeGeneratedCode(javaFile.getJavaFile(), testPayload);

        assertEquals(testPayload.getaByte(), deserialized.getaByte());
        assertEquals(testPayload.getaChar(), deserialized.getaChar());
        assertEquals(testPayload.getaDouble(), deserialized.getaDouble());
        assertEquals(testPayload.getaFloat(), deserialized.getaFloat());
        assertEquals(testPayload.getaLong(), deserialized.getaLong());
        assertEquals(testPayload.getAnInt(), deserialized.getAnInt());
        assertEquals(testPayload.getaShort(), deserialized.getaShort());
        assertEquals(testPayload.getaBoolean(), deserialized.getaBoolean());
    }


    @Test
    public void testClassWithNativeArrays() throws Exception {

        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithNativeArrays.class);

        ICodeGenerator impl = new CodeGeneratorImpl();
        JavaFileWrapper javaFile = impl.generate(codeGenSpec);
        assertNotNull(javaFile.getJavaFile());
        //javaFile.writeTo(System.out);
        ClassWithNativeArrays testPayload = new ClassWithNativeArrays();
        testPayload.randomize();
        ClassWithNativeArrays deserialized = invokeGeneratedCode(javaFile.getJavaFile(), testPayload);

        assertArrayEquals(testPayload.getAnInt(), deserialized.getAnInt());
        assertArrayEquals(testPayload.getaBoolean(), deserialized.getaBoolean());
        assertArrayEquals(testPayload.getaString(), deserialized.getaString());
    }

    //@Test
    public void testClassWithNativeCollections() throws Exception {

        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithNativeCollections.class);

        ICodeGenerator impl = new CodeGeneratorImpl();
        JavaFile javaFile = impl.generate(codeGenSpec).getJavaFile();
        assertNotNull(javaFile);
        javaFile.writeTo(System.out);
        /*ClassWithNativeCollections testPayload = new ClassWithNativeCollections();
        testPayload.randomize();
        ClassWithNativeCollections deserialized = invokeGeneratedCode(javaFile, testPayload);

        assertEquals(testPayload.getAnInt(), deserialized.getAnInt());
        assertEquals(testPayload.getaString(), deserialized.getaString());
        assertEquals(testPayload.getaMap(), deserialized.getaMap());
        assertEquals(testPayload.getaCollection(), deserialized.getaCollection());*/
    }
}