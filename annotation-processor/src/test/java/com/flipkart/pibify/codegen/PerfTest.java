package com.flipkart.pibify.codegen;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.pibify.test.data.ClassWithAutoboxFields;
import com.flipkart.pibify.test.data.ClassWithNativeFields;
import com.flipkart.pibify.test.data.ClassWithObjectCollections;
import com.flipkart.pibify.test.data.another.AnotherClassWithNativeFields;
import com.flipkart.pibify.test.util.SimpleCompiler;
import com.squareup.javapoet.JavaFile;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

/**
 * This class is used for running JMH between object mapper and pibify
 * Author bageshwar.pn
 * Date 11/08/24
 */
@SuppressWarnings("all")
public class PerfTest {

    BeanIntrospectorBasedCodeGenSpecCreator creator;
    CodeGenSpec codeGenSpec;
    ICodeGenerator impl;
    JavaFile javaFile;
    SimpleCompiler compiler;
    ObjectMapper objectMapper;

    public PerfTest() throws Exception {
        try {
            objectMapper = new ObjectMapper();
            creator = new BeanIntrospectorBasedCodeGenSpecCreator();
            codeGenSpec = creator.create(ClassWithObjectCollections.class);
            impl = new CodeGeneratorImpl();
            javaFile = impl.generate(codeGenSpec).getJavaFile();
            compiler = new SimpleCompiler();
            // load dependent class upfront
            Class[] dependent = new Class[]{ClassWithNativeFields.class, ClassWithAutoboxFields.class, AnotherClassWithNativeFields.class};
            for (Class clazz : dependent) {
                compiler.compile(impl.generate(creator.create(clazz)).getJavaFile().toJavaFileObject());
                Class<?> handlerClazz = compiler.loadClass("com.flipkart.pibify.generated." + clazz.getCanonicalName() + "Handler");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public byte[] pibify() throws Exception {
        ClassWithObjectCollections testPayload = new ClassWithObjectCollections();
        testPayload.randomize();
        return serializeOnly(compiler, javaFile, testPayload);
    }


    public byte[] jackson() throws Exception {
        ClassWithObjectCollections testPayload = new ClassWithObjectCollections();
        testPayload.randomize();
        return objectMapper.writeValueAsBytes(testPayload);

    }

    private <T> T invokeGeneratedCode(SimpleCompiler simpleCompiler, JavaFile javaFile, T data) throws Exception {
        simpleCompiler.compile(javaFile.toJavaFileObject());
        Class<?> handlerClazz = simpleCompiler.loadClass("com.flipkart.pibify.generated." + data.getClass().getCanonicalName() + "Handler");
        Object handlerInstance = handlerClazz.newInstance();
        Method serialize = handlerClazz.getDeclaredMethod("serialize", data.getClass());
        byte[] result = (byte[]) serialize.invoke(handlerInstance, data);

        Method deserialize = handlerClazz.getDeclaredMethod("deserialize", byte[].class);
        return (T) deserialize.invoke(handlerInstance, result);
    }

    private <T> byte[] serializeOnly(SimpleCompiler simpleCompiler, JavaFile javaFile, T data) throws Exception {
        simpleCompiler.compile(javaFile.toJavaFileObject());
        Class<?> handlerClazz = simpleCompiler.loadClass("com.flipkart.pibify.generated." + data.getClass().getCanonicalName() + "Handler");
        Object handlerInstance = handlerClazz.newInstance();
        Method serialize = handlerClazz.getDeclaredMethod("serialize", data.getClass());
        return (byte[]) serialize.invoke(handlerInstance, data);
    }

    @Test
    public void measurePibify() throws Exception {
        long start = System.currentTimeMillis();

        int size = 0;
        int loop = 5;
        for (int i = 0; i < loop; i++) {
            byte[] pibify = pibify();
            size += pibify.length;
        }

        System.out.println(((System.currentTimeMillis() - start) / loop) + "ms for pibify. avg payload " + (size / loop));
        size = 0;
        for (int i = 0; i < loop; i++) {
            byte[] jackson = jackson();
            size += jackson.length;
        }
        System.out.println(((System.currentTimeMillis() - start) / loop) + "ms for jackson. avg payload " + (size / loop));
    }
}
