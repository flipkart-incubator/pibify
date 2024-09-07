package com.flipkart.pibify.codegen;

import com.flipkart.pibify.test.data.ClassWithAutoboxFields;
import com.flipkart.pibify.test.data.ClassWithNativeArrays;
import com.flipkart.pibify.test.data.ClassWithNativeCollections;
import com.flipkart.pibify.test.data.ClassWithNativeCollectionsOfCollections;
import com.flipkart.pibify.test.data.ClassWithNativeFields;
import com.flipkart.pibify.test.data.ClassWithObjectCollections;
import com.flipkart.pibify.test.data.ClassWithReferences;
import com.flipkart.pibify.test.data.ClassWithReferencesToNativeFields;
import com.flipkart.pibify.test.data.another.AnotherClassWithNativeCollections;
import com.flipkart.pibify.test.data.another.AnotherClassWithNativeFields;
import com.flipkart.pibify.test.util.SimpleCompiler;
import com.squareup.javapoet.JavaFile;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("all")
class CodeGeneratorImplTest {

    private <T> T invokeGeneratedCode(SimpleCompiler simpleCompiler, JavaFile javaFile, T data) throws Exception {
        simpleCompiler.compile(javaFile.toJavaFileObject());
        Class<?> handlerClazz = simpleCompiler.loadClass("com.flipkart.pibify.generated." + data.getClass().getCanonicalName() + "Handler");
        Object handlerInstance = handlerClazz.newInstance();
        Method serialize = handlerClazz.getDeclaredMethod("serialize", data.getClass());
        byte[] result = (byte[]) serialize.invoke(handlerInstance, data);

        Method deserialize = handlerClazz.getDeclaredMethod("deserialize", byte[].class);
        return (T) deserialize.invoke(handlerInstance, result);
    }

    private <T> T invokeGeneratedCode(JavaFile javaFile, T data) throws Exception {
        SimpleCompiler simpleCompiler = new SimpleCompiler();
        return invokeGeneratedCode(simpleCompiler, javaFile, data);
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
        //javaFile.getJavaFile().writeTo(System.out);
        ClassWithNativeArrays testPayload = new ClassWithNativeArrays();
        testPayload.randomize();
        ClassWithNativeArrays deserialized = invokeGeneratedCode(javaFile.getJavaFile(), testPayload);

        assertArrayEquals(testPayload.getAnInt(), deserialized.getAnInt());
        assertArrayEquals(testPayload.getaBoolean(), deserialized.getaBoolean());
        assertArrayEquals(testPayload.getaString(), deserialized.getaString());
    }

    @Test
    public void testClassWithReferencesToNativeFields() throws Exception {

        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithReferencesToNativeFields.class);

        ICodeGenerator impl = new CodeGeneratorImpl();
        JavaFile javaFile = impl.generate(codeGenSpec).getJavaFile();
        assertNotNull(javaFile);
        //javaFile.writeTo(System.out);
        ClassWithReferencesToNativeFields testPayload = new ClassWithReferencesToNativeFields();
        testPayload.randomize();

        SimpleCompiler compiler = new SimpleCompiler();
        // load dependent class upfront
        compiler.compile(impl.generate(
                        creator.create(AnotherClassWithNativeFields.class))
                .getJavaFile().toJavaFileObject());
        Class<?> handlerClazz = compiler.loadClass("com.flipkart.pibify.generated." +
                AnotherClassWithNativeFields.class.getCanonicalName() + "Handler");

        ClassWithReferencesToNativeFields deserialized = invokeGeneratedCode(compiler, javaFile, testPayload);

        assertEquals(testPayload.getaString(), deserialized.getaString());
        assertEquals(testPayload.getReference().getaString(), deserialized.getReference().getaString());
        assertEquals(testPayload.getReference().getAnInt(), deserialized.getReference().getAnInt());
        assertEquals(testPayload.getReference().getaByte(), deserialized.getReference().getaByte());
        assertEquals(testPayload.getReference().getaChar(), deserialized.getReference().getaChar());
        assertEquals(testPayload.getReference().getaDouble(), deserialized.getReference().getaDouble());
        assertEquals(testPayload.getReference().getaFloat(), deserialized.getReference().getaFloat());
        assertEquals(testPayload.getReference().getaLong(), deserialized.getReference().getaLong());
        assertEquals(testPayload.getReference().getaShort(), deserialized.getReference().getaShort());
        assertEquals(testPayload.getReference().isaBoolean(), deserialized.getReference().isaBoolean());
    }

    @Test
    public void testClassWithNativeCollections() throws Exception {

        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithNativeCollections.class);

        ICodeGenerator impl = new CodeGeneratorImpl();
        JavaFile javaFile = impl.generate(codeGenSpec).getJavaFile();
        assertNotNull(javaFile);
        //javaFile.writeTo(System.out);
        ClassWithNativeCollections testPayload = new ClassWithNativeCollections();
        testPayload.randomize();

        ClassWithNativeCollections deserialized = invokeGeneratedCode(javaFile, testPayload);

        assertEquals(testPayload.getAnInt(), deserialized.getAnInt());
        assertEquals(testPayload.getaString(), deserialized.getaString());
        assertEquals(testPayload.getaMap(), deserialized.getaMap());
    }

    @Test
    public void testClassWithReferences() throws Exception {
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithReferences.class);

        ICodeGenerator impl = new CodeGeneratorImpl();
        JavaFile javaFile = impl.generate(codeGenSpec).getJavaFile();
        assertNotNull(javaFile);
        //javaFile.writeTo(System.out);
        ClassWithReferences testPayload = new ClassWithReferences();
        testPayload.randomize();

        SimpleCompiler compiler = new SimpleCompiler();
        // load dependent class upfront
        JavaFile javaFile1 = impl.generate(creator.create(AnotherClassWithNativeCollections.class)).getJavaFile();
        //javaFile1.writeTo(System.out);
        compiler.compile(javaFile1.toJavaFileObject());
        Class<?> handlerClazz = compiler.loadClass("com.flipkart.pibify.generated." +
                AnotherClassWithNativeCollections.class.getCanonicalName() + "Handler");

        ClassWithReferences deserialized = invokeGeneratedCode(compiler, javaFile, testPayload);

        assertEquals(testPayload.getaString(), deserialized.getaString());
        assertEquals(testPayload.getReference().getaString(), deserialized.getReference().getaString());
        assertEquals(testPayload.getReference().getAnInt(), deserialized.getReference().getAnInt());
        assertEquals(testPayload.getReference().getaMap(), deserialized.getReference().getaMap());
    }

    @Test
    public void testClassWithObjectCollections() throws Exception {
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithObjectCollections.class);

        ICodeGenerator impl = new CodeGeneratorImpl();
        JavaFile javaFile = impl.generate(codeGenSpec).getJavaFile();
        assertNotNull(javaFile);
        //javaFile.writeTo(System.out);
        ClassWithObjectCollections testPayload = new ClassWithObjectCollections();
        testPayload.randomize();

        SimpleCompiler compiler = new SimpleCompiler();
        // load dependent class upfront
        Class[] dependent = new Class[]{ClassWithNativeFields.class, ClassWithAutoboxFields.class, AnotherClassWithNativeFields.class};

        for (Class clazz : dependent) {
            compiler.compile(impl.generate(creator.create(clazz)).getJavaFile().toJavaFileObject());
            Class<?> handlerClazz = compiler.loadClass("com.flipkart.pibify.generated." + clazz.getCanonicalName() + "Handler");
        }

        ClassWithObjectCollections deserialized = invokeGeneratedCode(compiler, javaFile, testPayload);

        assertEquals(testPayload.getAutoboxFields().size(), deserialized.getAutoboxFields().size());
        for (Object obj : deserialized.getAutoboxFields().toArray()) {
            assertTrue(testPayload.getAutoboxFields().contains(obj));
        }

        assertEquals(testPayload.getNativeFields().size(), deserialized.getNativeFields().size());
        for (Object obj : deserialized.getNativeFields().toArray()) {
            assertTrue(testPayload.getNativeFields().contains(obj));
        }

        assertEquals(testPayload.getArrayOfOtherNativeFields().length, deserialized.getArrayOfOtherNativeFields().length);
        for (Object obj : deserialized.getArrayOfOtherNativeFields()) {
            assertTrue(Arrays.asList(testPayload.getArrayOfOtherNativeFields()).contains(obj));
        }

        assertEquals(testPayload.getMapOfObjects(), deserialized.getMapOfObjects());
    }

    @Test
    public void testClassWithNativeCollectionsOfCollections() throws Exception {

        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithNativeCollectionsOfCollections.class);

        ICodeGenerator impl = new CodeGeneratorImpl();
        JavaFile javaFile = impl.generate(codeGenSpec).getJavaFile();
        assertNotNull(javaFile);
        //javaFile.writeTo(System.out);
        ClassWithNativeCollectionsOfCollections testPayload = new ClassWithNativeCollectionsOfCollections();
        testPayload.randomize();

        ClassWithNativeCollectionsOfCollections deserialized = invokeGeneratedCode(javaFile, testPayload);

        assertEquals(testPayload.getAnInt(), deserialized.getAnInt());
        assertEquals(testPayload.getaString(), deserialized.getaString());
        assertEquals(testPayload.getaMap(), deserialized.getaMap());
    }
}