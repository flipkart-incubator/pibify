package com.flipkart.pibify.codegen;

import com.flipkart.pibify.codegen.log.SpecGenLogLevel;
import com.flipkart.pibify.codegen.stub.PibifyObjectHandler;
import com.flipkart.pibify.core.PibifyConfiguration;
import com.flipkart.pibify.test.data.ClassForTestingNullValues;
import com.flipkart.pibify.test.data.ClassHierarchy2A;
import com.flipkart.pibify.test.data.ClassHierarchy2B;
import com.flipkart.pibify.test.data.ClassHierarchy3A;
import com.flipkart.pibify.test.data.ClassWithAutoboxFields;
import com.flipkart.pibify.test.data.ClassWithCollectionReference;
import com.flipkart.pibify.test.data.ClassWithCollectionsOfEnums;
import com.flipkart.pibify.test.data.ClassWithEnums;
import com.flipkart.pibify.test.data.ClassWithInnerClasses;
import com.flipkart.pibify.test.data.ClassWithInterestingFieldNames;
import com.flipkart.pibify.test.data.ClassWithMapReference;
import com.flipkart.pibify.test.data.ClassWithNativeArrays;
import com.flipkart.pibify.test.data.ClassWithNativeCollections;
import com.flipkart.pibify.test.data.ClassWithNativeCollectionsOfCollections;
import com.flipkart.pibify.test.data.ClassWithNativeFields;
import com.flipkart.pibify.test.data.ClassWithNoBeanInfo;
import com.flipkart.pibify.test.data.ClassWithNoFields;
import com.flipkart.pibify.test.data.ClassWithObjectCollections;
import com.flipkart.pibify.test.data.ClassWithObjectReference;
import com.flipkart.pibify.test.data.ClassWithReferences;
import com.flipkart.pibify.test.data.ClassWithReferencesToNativeFields;
import com.flipkart.pibify.test.data.ClassWithSchemaChange1;
import com.flipkart.pibify.test.data.ClassWithSchemaChange2;
import com.flipkart.pibify.test.data.ClassWithUnresolvedGenericType;
import com.flipkart.pibify.test.data.SubClassOfClassWithTypeParameterReference;
import com.flipkart.pibify.test.data.another.AnotherClassWithNativeCollections;
import com.flipkart.pibify.test.data.another.AnotherClassWithNativeFields;
import com.flipkart.pibify.test.data.generics.ListClassLevel1;
import com.flipkart.pibify.test.data.generics.ListClassLevel2;
import com.flipkart.pibify.test.data.generics.ListClassLevel3;
import com.flipkart.pibify.test.data.generics.ListClassLevel4;
import com.flipkart.pibify.test.data.generics.MapClassLevel1;
import com.flipkart.pibify.test.data.generics.MapClassLevel2;
import com.flipkart.pibify.test.data.generics.MapClassLevel3;
import com.flipkart.pibify.test.data.generics.MapClassLevel4;
import com.flipkart.pibify.test.data.generics.MapClassLevel5;
import com.flipkart.pibify.test.data.generics.MapClassLevel6;
import com.flipkart.pibify.test.util.PibifyHandlerCacheForTest;
import com.flipkart.pibify.test.util.SimpleCompiler;
import com.squareup.javapoet.JavaFile;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

@SuppressWarnings("all")
public class CodeGeneratorImplTest {

    // TODO: Cleaup tests to handle dependent classes better
    // TODO: Use equals of test class instead of manual comparision of members

    @BeforeAll
    public static void setup() {
        PibifyConfiguration.builder().build();
    }

    private static <T> T invokeGeneratedCode(SimpleCompiler simpleCompiler, JavaFile javaFile, T data) throws Exception {
        // Not reusing `serialize` and `deserialize` to save on test run time
        simpleCompiler.compile(javaFile.toJavaFileObject());
        Class<?> handlerClazz = simpleCompiler.loadClass("com.flipkart.pibify.generated." + data.getClass().getCanonicalName() + "Handler");
        Object handlerInstance = handlerClazz.newInstance();
        Method serialize = handlerClazz.getDeclaredMethod("serialize", data.getClass());
        byte[] result = (byte[]) serialize.invoke(handlerInstance, data);

        Method deserialize = handlerClazz.getDeclaredMethod("deserialize", byte[].class, Class.class);
        return (T) deserialize.invoke(handlerInstance, result, data.getClass());
    }

    public static <T> T deserialize(SimpleCompiler simpleCompiler, JavaFile javaFile, T data, byte[] result) throws Exception {
        simpleCompiler.compile(javaFile.toJavaFileObject());
        Class<?> handlerClazz = simpleCompiler.loadClass("com.flipkart.pibify.generated." + data.getClass().getCanonicalName() + "Handler");
        Object handlerInstance = handlerClazz.newInstance();
        Method deserialize = handlerClazz.getDeclaredMethod("deserialize", byte[].class, Class.class);
        return (T) deserialize.invoke(handlerInstance, result, data.getClass());
    }

    private static <T> T invokeGeneratedCode(JavaFile javaFile, T data) throws Exception {
        SimpleCompiler simpleCompiler = new SimpleCompiler();
        return invokeGeneratedCode(simpleCompiler, javaFile, data);
    }

    public static <T> byte[] serialize(SimpleCompiler simpleCompiler, JavaFile javaFile, T data) throws Exception {
        simpleCompiler.compile(javaFile.toJavaFileObject());
        Class<?> handlerClazz = simpleCompiler.loadClass("com.flipkart.pibify.generated." + data.getClass().getCanonicalName() + "Handler");
        Object handlerInstance = handlerClazz.newInstance();
        Method serialize = handlerClazz.getDeclaredMethod("serialize", data.getClass());
        byte[] result = (byte[]) serialize.invoke(handlerInstance, data);
        return result;
    }

    @Test
    public void testClassWithNativeFields() throws Exception {

        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithNativeFields.class);

        ICodeGenerator impl = new CodeGeneratorImpl(PibifyHandlerCacheForTest.class.getCanonicalName());
        JavaFileWrapper javaFile = impl.generate(codeGenSpec);
        assertNotNull(javaFile.getJavaFile());
        //javaFile.getJavaFile().writeTo(System.out);
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

        ICodeGenerator impl = new CodeGeneratorImpl(PibifyHandlerCacheForTest.class.getCanonicalName());
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

        ICodeGenerator impl = new CodeGeneratorImpl(PibifyHandlerCacheForTest.class.getCanonicalName());
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

        ICodeGenerator impl = new CodeGeneratorImpl(PibifyHandlerCacheForTest.class.getCanonicalName());
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

        ICodeGenerator impl = new CodeGeneratorImpl(PibifyHandlerCacheForTest.class.getCanonicalName());
        JavaFile javaFile = impl.generate(codeGenSpec).getJavaFile();
        assertNotNull(javaFile);
        //javaFile.writeTo(System.out);
        ClassWithNativeCollections testPayload = new ClassWithNativeCollections();
        testPayload.randomize();

        ClassWithNativeCollections deserialized = invokeGeneratedCode(javaFile, testPayload);

        assertEquals(testPayload.getAnInt(), deserialized.getAnInt());
        assertEquals(testPayload.getaString(), deserialized.getaString());
        assertEquals(testPayload.getaMap(), deserialized.getaMap());
        assertArrayEquals(testPayload.getListOfBytes().get(0), deserialized.getListOfBytes().get(0));
        assertArrayEquals(testPayload.getListOfBytes().get(1), deserialized.getListOfBytes().get(1));
    }

    @Test
    public void testClassWithReferences() throws Exception {
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithReferences.class);

        ICodeGenerator impl = new CodeGeneratorImpl(PibifyHandlerCacheForTest.class.getCanonicalName());
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

        ICodeGenerator impl = new CodeGeneratorImpl(PibifyHandlerCacheForTest.class.getCanonicalName());
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

        assertEquals(testPayload.getAutoboxFields(), deserialized.getAutoboxFields());
        assertEquals(testPayload.getNativeFields(), deserialized.getNativeFields());
        assertArrayEquals(testPayload.getArrayOfOtherNativeFields(), deserialized.getArrayOfOtherNativeFields());
        assertEquals(testPayload.getMapOfObjects(), deserialized.getMapOfObjects());
        assertEquals(testPayload.getBigDecimalList(), deserialized.getBigDecimalList());
        assertEquals(testPayload.getBigDecimalMap(), deserialized.getBigDecimalMap());
    }

    @Test
    public void testClassWithNativeCollectionsOfCollections() throws Exception {

        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithNativeCollectionsOfCollections.class);

        ICodeGenerator impl = new CodeGeneratorImpl(PibifyHandlerCacheForTest.class.getCanonicalName());
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

    @Test
    public void testClassWithNoFields() throws Exception {
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithNoFields.class);
        ICodeGenerator impl = new CodeGeneratorImpl(PibifyHandlerCacheForTest.class.getCanonicalName());
        assertThrows(CodeGenException.class, () -> impl.generate(codeGenSpec), "com.flipkart.pibify.test.data.ClassWithNoFields does not contain any pibify fields");
    }

    @Test
    public void testAllNullValueInPayload() throws Exception {
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassForTestingNullValues.class);
        ICodeGenerator impl = new CodeGeneratorImpl(PibifyHandlerCacheForTest.class.getCanonicalName());
        JavaFile javaFile = impl.generate(codeGenSpec).getJavaFile();
        assertNotNull(javaFile);
        //javaFile.writeTo(System.out);

        SimpleCompiler compiler = new SimpleCompiler();
        // load dependent class upfront
        Class[] dependent = new Class[]{AnotherClassWithNativeCollections.class, ClassWithReferences.class};

        for (Class clazz : dependent) {
            JavaFile javaFile1 = impl.generate(creator.create(clazz)).getJavaFile();
            //javaFile1.writeTo(System.out);
            compiler.compile(javaFile1.toJavaFileObject());
            Class<?> handlerClazz = compiler.loadClass("com.flipkart.pibify.generated." + clazz.getCanonicalName() + "Handler");
        }

        ClassForTestingNullValues testPayload = new ClassForTestingNullValues();
        ClassForTestingNullValues deserialized = invokeGeneratedCode(compiler, javaFile, testPayload);
        for (CodeGenSpec.FieldSpec field : codeGenSpec.getFields()) {
            if (CodeGenUtil.isNotNative(field.getType().getNativeType())) {
                assertNull(ClassForTestingNullValues.class.getMethod(field.getGetter()).invoke(deserialized));
            }
        }
    }

    @Test
    public void testSomeNullValueInPayload() throws Exception {
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassForTestingNullValues.class);
        ICodeGenerator impl = new CodeGeneratorImpl(PibifyHandlerCacheForTest.class.getCanonicalName());
        JavaFile javaFile = impl.generate(codeGenSpec).getJavaFile();
        assertNotNull(javaFile);
        //javaFile.writeTo(System.out);

        SimpleCompiler compiler = new SimpleCompiler();
        // load dependent class upfront
        Class[] dependent = new Class[]{AnotherClassWithNativeCollections.class, ClassWithReferences.class};

        for (Class clazz : dependent) {
            JavaFile javaFile1 = impl.generate(creator.create(clazz)).getJavaFile();
            //javaFile1.writeTo(System.out);
            compiler.compile(javaFile1.toJavaFileObject());
            Class<?> handlerClazz = compiler.loadClass("com.flipkart.pibify.generated." + clazz.getCanonicalName() + "Handler");
        }

        ClassForTestingNullValues testPayload = new ClassForTestingNullValues();
        testPayload.setaString("hello");
        testPayload.setaStringList(Arrays.asList(null, "hello", null, "world", null, ""));
        testPayload.setaString1(Arrays.asList(
                null,
                Arrays.asList(null, "hello", "world"),
                null,
                Arrays.asList("hello", null, "world"),
                null,
                Arrays.asList("hello", "world", null),
                null,
                new ArrayList<>()
        ));

        Set<String> nonNullFields = new HashSet<>(Arrays.asList("aString", "aStringList", "aString1"));
        ClassForTestingNullValues deserialized = invokeGeneratedCode(compiler, javaFile, testPayload);

        assertEquals(testPayload.getaString(), deserialized.getaString());
        assertEquals(Arrays.asList("hello", "world", ""), deserialized.getaStringList());
        assertEquals(Arrays.asList(
                Arrays.asList("hello", "world"),
                Arrays.asList("hello", "world"),
                Arrays.asList("hello", "world"),
                new ArrayList<>()), deserialized.getaString1());

        for (CodeGenSpec.FieldSpec field : codeGenSpec.getFields()) {
            if (CodeGenUtil.isNotNative(field.getType().getNativeType()) && !nonNullFields.contains(field.getName())) {
                assertNull(ClassForTestingNullValues.class.getMethod(field.getGetter()).invoke(deserialized));
            }
        }
    }

    @Test
    public void testClassHierarchy() throws Exception {

        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassHierarchy2B.class);

        ICodeGenerator impl = new CodeGeneratorImpl(PibifyHandlerCacheForTest.class.getCanonicalName());
        JavaFile javaFile = impl.generate(codeGenSpec).getJavaFile();
        assertNotNull(javaFile);
        //javaFile.writeTo(System.out);
        ClassHierarchy2B testPayload = new ClassHierarchy2B();
        testPayload.randomize();

        ClassHierarchy2B deserialized = invokeGeneratedCode(javaFile, testPayload);

        assertEquals(testPayload.getMember1(), deserialized.getMember1());
        assertEquals(testPayload.getStr1(), deserialized.getStr1());
        assertEquals(testPayload.getStr2(), deserialized.getStr2());
        assertEquals(testPayload.getStr3(), deserialized.getStr3());
        assertEquals(testPayload.getStr4(), deserialized.getStr4());

        codeGenSpec = creator.create(ClassHierarchy2A.class);

        javaFile = impl.generate(codeGenSpec).getJavaFile();
        assertNotNull(javaFile);
        //javaFile.writeTo(System.out);
        ClassHierarchy2A testPayload1 = new ClassHierarchy2A();
        testPayload1.randomize();

        ClassHierarchy2A deserialized1 = invokeGeneratedCode(javaFile, testPayload1);

        assertEquals(testPayload1.getMember2(), deserialized1.getMember2());
        assertEquals(testPayload1.getStr1(), deserialized1.getStr1());
        assertEquals(testPayload1.getStr2(), deserialized1.getStr2());
        assertEquals(testPayload1.getStr3(), deserialized1.getStr3());
        assertEquals(testPayload1.getStr4(), deserialized1.getStr4());

        codeGenSpec = creator.create(ClassHierarchy3A.class);

        javaFile = impl.generate(codeGenSpec).getJavaFile();
        assertNotNull(javaFile);
        //javaFile.writeTo(System.out);
        ClassHierarchy3A testPayload2 = new ClassHierarchy3A();
        testPayload1.randomize();

        ClassHierarchy3A deserialized2 = invokeGeneratedCode(javaFile, testPayload2);

        assertEquals(testPayload2.getMember2(), deserialized2.getMember2());
        assertEquals(testPayload2.getMember3(), deserialized2.getMember3());
        assertEquals(testPayload2.getStr1(), deserialized2.getStr1());
        assertEquals(testPayload2.getStr2(), deserialized2.getStr2());
        assertEquals(testPayload2.getStr3(), deserialized2.getStr3());
        assertEquals(testPayload2.getStr4(), deserialized2.getStr4());
    }

    @Test
    public void testClassWithEnums() throws Exception {

        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithEnums.class);

        ICodeGenerator impl = new CodeGeneratorImpl(PibifyHandlerCacheForTest.class.getCanonicalName());
        JavaFile javaFile = impl.generate(codeGenSpec).getJavaFile();
        assertNotNull(javaFile);
        //javaFile.writeTo(System.out);
        ClassWithEnums testPayload = new ClassWithEnums();
        testPayload.randomize();

        ClassWithEnums deserialized = invokeGeneratedCode(javaFile, testPayload);

        assertEquals(testPayload.getEnumA(), deserialized.getEnumA());
        assertEquals(testPayload.getEnumB(), deserialized.getEnumB());
        assertEquals(testPayload.getNullEnum(), deserialized.getNullEnum());
    }

    @Test
    public void testClassWithCollectionsOfEnums() throws Exception {

        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithCollectionsOfEnums.class);

        ICodeGenerator impl = new CodeGeneratorImpl(PibifyHandlerCacheForTest.class.getCanonicalName());
        JavaFile javaFile = impl.generate(codeGenSpec).getJavaFile();
        assertNotNull(javaFile);
        //javaFile.writeTo(System.out);
        ClassWithCollectionsOfEnums testPayload = new ClassWithCollectionsOfEnums();
        testPayload.randomize();

        ClassWithCollectionsOfEnums deserialized = invokeGeneratedCode(javaFile, testPayload);

        assertEquals(testPayload.getListOfEnums(), deserialized.getListOfEnums());
        assertEquals(testPayload.getEnumMap(), deserialized.getEnumMap());
        assertEquals(testPayload.getMapOfEnums(), deserialized.getMapOfEnums());
        assertArrayEquals(testPayload.getArrayOfEnums(), deserialized.getArrayOfEnums());
    }


    @Test
    public void testClassWithInnerClasses() throws Exception {

        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithInnerClasses.class);

        ICodeGenerator impl = new CodeGeneratorImpl(PibifyHandlerCacheForTest.class.getCanonicalName());
        JavaFile javaFile = impl.generate(codeGenSpec).getJavaFile();
        assertNotNull(javaFile);
        //javaFile.writeTo(System.out);
        ClassWithInnerClasses testPayload = new ClassWithInnerClasses();
        testPayload.randomize();

        ClassWithInnerClasses deserialized = invokeGeneratedCode(javaFile, testPayload);

        assertEquals(testPayload.getStr1(), deserialized.getStr1());
        assertEquals(testPayload.getStaticInnerClass().getStr3(), deserialized.getStaticInnerClass().getStr3());
        assertEquals(testPayload.getStaticInnerClass().getStaticInnerInnerClass().getStr33(), deserialized.getStaticInnerClass().getStaticInnerInnerClass().getStr33());
    }


    @Test
    public void testSubClassOfClassWithTypeParameterReference() throws Exception {

        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(SubClassOfClassWithTypeParameterReference.class);

        ICodeGenerator impl = new CodeGeneratorImpl(PibifyHandlerCacheForTest.class.getCanonicalName());
        JavaFile javaFile = impl.generate(codeGenSpec).getJavaFile();
        assertNotNull(javaFile);
        //javaFile.writeTo(System.out);
        SubClassOfClassWithTypeParameterReference testPayload = new SubClassOfClassWithTypeParameterReference();
        testPayload.randomize();

        SimpleCompiler compiler = new SimpleCompiler();
        // load dependent class upfront
        Class[] dependent = new Class[]{AnotherClassWithNativeCollections.class, ClassWithReferences.class};

        for (Class clazz : dependent) {
            JavaFile javaFile1 = impl.generate(creator.create(clazz)).getJavaFile();
            //javaFile1.writeTo(System.out);
            compiler.compile(javaFile1.toJavaFileObject());
            Class<?> handlerClazz = compiler.loadClass("com.flipkart.pibify.generated." + clazz.getCanonicalName() + "Handler");
        }

        SubClassOfClassWithTypeParameterReference deserialized = invokeGeneratedCode(compiler, javaFile, testPayload);
        assertEquals(testPayload, deserialized);
    }

    @Test
    public void testClassWithUnresolvedGenericType() throws Exception {

        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithUnresolvedGenericType.class);

        ICodeGenerator impl = new CodeGeneratorImpl(PibifyHandlerCacheForTest.class.getCanonicalName());
        JavaFile javaFile = impl.generate(codeGenSpec).getJavaFile();
        assertNotNull(javaFile);
        //javaFile.writeTo(System.out);
        ClassWithUnresolvedGenericType<Double> testPayload = new ClassWithUnresolvedGenericType<>();
        testPayload.randomize(Math.random());

        SimpleCompiler compiler = new SimpleCompiler();
        // load dependent class upfront
        Class[] dependent = new Class[]{AnotherClassWithNativeCollections.class, ClassWithReferences.class};

        for (Class clazz : dependent) {
            JavaFile javaFile1 = impl.generate(creator.create(clazz)).getJavaFile();
            //javaFile1.writeTo(System.out);
            compiler.compile(javaFile1.toJavaFileObject());
            Class<?> handlerClazz = compiler.loadClass("com.flipkart.pibify.generated." + clazz.getCanonicalName() + "Handler");
        }

        ClassWithUnresolvedGenericType deserialized = invokeGeneratedCode(compiler, javaFile, testPayload);
        assertEquals(testPayload.getStr(), deserialized.getStr());
        assertEquals(testPayload.getList(), deserialized.getList());
        assertEquals(testPayload.getList2(), deserialized.getList2());
        assertEquals(testPayload.getMap(), deserialized.getMap());
        assertEquals(testPayload.getMap2(), deserialized.getMap2());
        assertEquals(testPayload.getGenericTypeReference(), deserialized.getGenericTypeReference());
    }


    @Test
    public void testClassWithObjectReference() throws Exception {
        PibifyObjectHandler.forTest = true;
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithObjectReference.class);

        ICodeGenerator impl = new CodeGeneratorImpl(PibifyHandlerCacheForTest.class.getCanonicalName());
        JavaFile javaFile = impl.generate(codeGenSpec).getJavaFile();
        assertNotNull(javaFile);
        //javaFile.writeTo(System.out);
        ClassWithObjectReference testPayload = new ClassWithObjectReference();
        testPayload.randomize();

        Class[] dependent = new Class[]{ClassWithNativeFields.class};
        SimpleCompiler compiler = SimpleCompiler.INSTANCE;
        for (Class clazz : dependent) {
            JavaFile javaFile1 = impl.generate(creator.create(clazz)).getJavaFile();
            //javaFile1.writeTo(System.out);
            compiler.compile(javaFile1.toJavaFileObject());
            Class<?> handlerClazz = compiler.loadClass("com.flipkart.pibify.generated." + clazz.getCanonicalName() + "Handler");
        }

        ClassWithObjectReference deserialized = invokeGeneratedCode(compiler, javaFile, testPayload);
        assertEquals(testPayload, deserialized);
        PibifyObjectHandler.forTest = false;
    }

    @Test
    public void testClassWithCollectionReference() throws Exception {
        PibifyObjectHandler.forTest = true;
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithCollectionReference.class);

        ICodeGenerator impl = new CodeGeneratorImpl(PibifyHandlerCacheForTest.class.getCanonicalName());
        JavaFile javaFile = impl.generate(codeGenSpec).getJavaFile();
        assertNotNull(javaFile);
        //javaFile.writeTo(System.out);
        ClassWithCollectionReference testPayload = new ClassWithCollectionReference();
        testPayload.randomize();

        try {
            invokeGeneratedCode(javaFile, testPayload);
            fail();
        } catch (Exception e) {
            assertEquals("com.flipkart.pibify.codegen.PibifyCodeExecException: java.lang.UnsupportedOperationException: Arrays, maps and collections not supported on object references",
                    e.getCause().getMessage());
        }
    }

    @Test
    public void testClassWithMapReference() throws Exception {
        PibifyObjectHandler.forTest = true;
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithMapReference.class);

        ICodeGenerator impl = new CodeGeneratorImpl(PibifyHandlerCacheForTest.class.getCanonicalName());
        JavaFile javaFile = impl.generate(codeGenSpec).getJavaFile();
        assertNotNull(javaFile);
        //javaFile.writeTo(System.out);
        ClassWithMapReference testPayload = new ClassWithMapReference();
        testPayload.randomize();

        try {
            invokeGeneratedCode(javaFile, testPayload);
            fail();
        } catch (Exception e) {
            assertEquals("com.flipkart.pibify.codegen.PibifyCodeExecException: java.lang.UnsupportedOperationException: Arrays, maps and collections not supported on object references",
                    e.getCause().getMessage());
        }
    }

    @Test
    public void testClassWithSchemaChange2() throws Exception {
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithSchemaChange1.class);

        ICodeGenerator impl = new CodeGeneratorImpl(PibifyHandlerCacheForTest.class.getCanonicalName());
        JavaFile javaFile = impl.generate(codeGenSpec).getJavaFile();
        assertNotNull(javaFile);
        //javaFile.writeTo(System.out);
        ClassWithSchemaChange1 testPayload = new ClassWithSchemaChange1();
        testPayload.randomize();

        byte[] bytes = serialize(SimpleCompiler.INSTANCE, javaFile, testPayload);

        codeGenSpec = creator.create(ClassWithSchemaChange2.class);
        javaFile = impl.generate(codeGenSpec).getJavaFile();
        //javaFile.writeTo(System.out);
        assertNotNull(javaFile);

        ClassWithSchemaChange2 deserialized = deserialize(SimpleCompiler.INSTANCE, javaFile, new ClassWithSchemaChange2(), bytes);
        assertEquals(testPayload.getStr1(), deserialized.getStr1());
        // although names are same, the index has changed
        assertNotEquals(testPayload.getStr2(), deserialized.getStr2());
    }

    @Test
    public void testClassWithInterestingFieldNames() throws Exception {
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithInterestingFieldNames.class);
        assertNotNull(codeGenSpec);
        assertNotNull(creator.getLogsForCurrentEntity());
        assertEquals(SpecGenLogLevel.INFO, creator.status(ClassWithInterestingFieldNames.class));
        assertEquals(5, codeGenSpec.getFields().size());

        ICodeGenerator impl = new CodeGeneratorImpl(PibifyHandlerCacheForTest.class.getCanonicalName());
        JavaFile javaFile = impl.generate(codeGenSpec).getJavaFile();
        assertNotNull(javaFile);
        //javaFile.writeTo(System.out);
        ClassWithInterestingFieldNames testPayload = new ClassWithInterestingFieldNames();
        testPayload.randomize();

        ClassWithInterestingFieldNames deserialized = invokeGeneratedCode(javaFile, testPayload);
        assertEquals(testPayload, deserialized);
    }

    @Test
    public void testListClassLevel2() throws Exception {
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ListClassLevel2.class);
        assertNotNull(codeGenSpec);

        ICodeGenerator impl = new CodeGeneratorImpl(PibifyHandlerCacheForTest.class.getCanonicalName());
        JavaFile javaFile = impl.generate(codeGenSpec).getJavaFile();
        assertNotNull(javaFile);
        //javaFile.writeTo(System.out);
        ListClassLevel2 testPayload = new ListClassLevel2();
        testPayload.randomize("str" + Math.random());

        ListClassLevel2 deserialized = invokeGeneratedCode(javaFile, testPayload);
        assertEquals(testPayload.getStr(), deserialized.getStr());
        assertEquals(testPayload.getObj(), deserialized.getObj());
        assertEquals(testPayload, deserialized);
    }

    @Test
    public void testListClassLevel1() throws Exception {
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ListClassLevel1.class);
        assertNotNull(codeGenSpec);

        ICodeGenerator impl = new CodeGeneratorImpl(PibifyHandlerCacheForTest.class.getCanonicalName());
        JavaFile javaFile = impl.generate(codeGenSpec).getJavaFile();
        assertNotNull(javaFile);
        //javaFile.writeTo(System.out);
        ListClassLevel1 testPayload = new ListClassLevel1();
        testPayload.randomize("str" + Math.random(), "str" + Math.random());

        ListClassLevel1 deserialized = invokeGeneratedCode(javaFile, testPayload);
        assertEquals(testPayload.getStr(), deserialized.getStr());
        assertEquals(testPayload.getObj(), deserialized.getObj());
        assertEquals(testPayload, deserialized);
    }

    @Test
    public void testListClassLevel3() throws Exception {
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ListClassLevel3.class);
        assertNotNull(codeGenSpec);

        ICodeGenerator impl = new CodeGeneratorImpl(PibifyHandlerCacheForTest.class.getCanonicalName());
        JavaFile javaFile = impl.generate(codeGenSpec).getJavaFile();
        assertNotNull(javaFile);
        //javaFile.writeTo(System.out);
        ListClassLevel3 testPayload = new ListClassLevel3();
        testPayload.randomize("str" + Math.random());

        ListClassLevel3 deserialized = invokeGeneratedCode(javaFile, testPayload);
        assertEquals(testPayload.getStr(), deserialized.getStr());
        assertEquals(testPayload.getObj(), deserialized.getObj());
        assertEquals(testPayload, deserialized);
    }

    @Test
    public void testListClassLevel4() throws Exception {
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ListClassLevel4.class);
        assertNotNull(codeGenSpec);

        ICodeGenerator impl = new CodeGeneratorImpl(PibifyHandlerCacheForTest.class.getCanonicalName());
        JavaFile javaFile = impl.generate(codeGenSpec).getJavaFile();
        assertNotNull(javaFile);
        //javaFile.writeTo(System.out);
        ListClassLevel4 testPayload = new ListClassLevel4();
        testPayload.randomize("str" + Math.random(), "str" + Math.random());

        ListClassLevel4 deserialized = invokeGeneratedCode(javaFile, testPayload);
        assertEquals(testPayload.getStr(), deserialized.getStr());
        assertEquals(testPayload.getObj(), deserialized.getObj());
        assertEquals(testPayload, deserialized);
    }

    @Test
    public void testMapClassLevel1() throws Exception {
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(MapClassLevel1.class);
        assertNotNull(codeGenSpec);

        ICodeGenerator impl = new CodeGeneratorImpl(PibifyHandlerCacheForTest.class.getCanonicalName());
        JavaFile javaFile = impl.generate(codeGenSpec).getJavaFile();
        assertNotNull(javaFile);
        //javaFile.writeTo(System.out);
        MapClassLevel1<Double, Double> testPayload = new MapClassLevel1<>();
        testPayload.randomize(Math.random(), Math.random());

        MapClassLevel1<Double, Double> deserialized = invokeGeneratedCode(javaFile, testPayload);
        assertEquals(testPayload.getStr(), deserialized.getStr());
        assertEquals(testPayload, deserialized);
    }

    @Test
    public void testMapClassLevel2() throws Exception {
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(MapClassLevel2.class);
        assertNotNull(codeGenSpec);

        ICodeGenerator impl = new CodeGeneratorImpl(PibifyHandlerCacheForTest.class.getCanonicalName());
        JavaFile javaFile = impl.generate(codeGenSpec).getJavaFile();
        assertNotNull(javaFile);
        //javaFile.writeTo(System.out);
        MapClassLevel2<Double> testPayload = new MapClassLevel2<>();
        testPayload.randomize(Math.random());

        MapClassLevel2<Double> deserialized = invokeGeneratedCode(javaFile, testPayload);
        assertEquals(testPayload.getStr(), deserialized.getStr());
        assertEquals(testPayload, deserialized);
    }

    @Test
    public void testMapClassLevel3() throws Exception {
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(MapClassLevel3.class);
        assertNotNull(codeGenSpec);

        ICodeGenerator impl = new CodeGeneratorImpl(PibifyHandlerCacheForTest.class.getCanonicalName());
        JavaFile javaFile = impl.generate(codeGenSpec).getJavaFile();
        assertNotNull(javaFile);
        //javaFile.writeTo(System.out);
        MapClassLevel3<Double> testPayload = new MapClassLevel3<>();
        testPayload.randomize(Math.random());

        MapClassLevel3<Double> deserialized = invokeGeneratedCode(javaFile, testPayload);
        assertEquals(testPayload.getStr(), deserialized.getStr());
        assertEquals(testPayload, deserialized);
    }

    @Test
    public void testMapClassLevel4() throws Exception {
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(MapClassLevel4.class);
        assertNotNull(codeGenSpec);

        ICodeGenerator impl = new CodeGeneratorImpl(PibifyHandlerCacheForTest.class.getCanonicalName());
        JavaFile javaFile = impl.generate(codeGenSpec).getJavaFile();
        assertNotNull(javaFile);
        //javaFile.writeTo(System.out);
        MapClassLevel4<Double, String> testPayload = new MapClassLevel4<>();
        testPayload.randomize();
        testPayload.randomize(Math.random(), "str" + Math.random());

        MapClassLevel4<Double, String> deserialized = invokeGeneratedCode(javaFile, testPayload);
        assertEquals(testPayload.getStr(), deserialized.getStr());
        assertEquals(testPayload.getStr1(), deserialized.getStr1());
        assertEquals(testPayload, deserialized);

        // Trying a different variation of generics
        MapClassLevel4<String, Double> testPayload2 = new MapClassLevel4<>();
        testPayload2.randomize();
        testPayload2.randomize("str" + Math.random(), Math.random());

        MapClassLevel4<String, Double> deserialized2 = invokeGeneratedCode(javaFile, testPayload2);
        assertEquals(testPayload2.getStr(), deserialized2.getStr());
        assertEquals(testPayload2.getStr1(), deserialized2.getStr1());
        assertEquals(testPayload2, deserialized2);

        Class[] dependent = new Class[]{ClassWithNativeFields.class};
        SimpleCompiler compiler = SimpleCompiler.INSTANCE;
        for (Class clazz : dependent) {
            JavaFile javaFile1 = impl.generate(creator.create(clazz)).getJavaFile();
            compiler.compile(javaFile1.toJavaFileObject());
            Class<?> handlerClazz = compiler.loadClass("com.flipkart.pibify.generated." + clazz.getCanonicalName() + "Handler");
            assertNotNull(handlerClazz);
        }

        // Trying a different variation of generics
        MapClassLevel4<String, ClassWithNativeFields> testPayload3 = new MapClassLevel4<>();
        testPayload3.randomize();
        testPayload3.randomize("str" + Math.random(), new ClassWithNativeFields().randomize());

        PibifyObjectHandler.forTest = true;
        MapClassLevel4<String, ClassWithNativeFields> deserialized3 = invokeGeneratedCode(compiler, javaFile, testPayload3);
        PibifyObjectHandler.forTest = false;
        assertEquals(testPayload3.getStr(), deserialized3.getStr());
        assertEquals(testPayload3.getStr1(), deserialized3.getStr1());
        assertEquals(testPayload3, deserialized3);
    }

    @Test
    public void testMapClassLevel5() throws Exception {
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(MapClassLevel5.class);
        assertNotNull(codeGenSpec);

        ICodeGenerator impl = new CodeGeneratorImpl(PibifyHandlerCacheForTest.class.getCanonicalName());
        JavaFile javaFile = impl.generate(codeGenSpec).getJavaFile();
        assertNotNull(javaFile);
        //javaFile.writeTo(System.out);
        MapClassLevel5 testPayload = new MapClassLevel5();
        testPayload.randomize();

        MapClassLevel5 deserialized = invokeGeneratedCode(javaFile, testPayload);
        assertEquals(testPayload.getStr(), deserialized.getStr());
        assertEquals(testPayload.getStr2(), deserialized.getStr2());
        assertEquals(testPayload, deserialized);
    }

    @Test
    public void testMapClassLevel6() throws Exception {
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(MapClassLevel6.class);
        assertNotNull(codeGenSpec);

        ICodeGenerator impl = new CodeGeneratorImpl(PibifyHandlerCacheForTest.class.getCanonicalName());
        JavaFile javaFile = impl.generate(codeGenSpec).getJavaFile();
        assertNotNull(javaFile);
        //javaFile.writeTo(System.out);
        MapClassLevel6<Integer> testPayload = new MapClassLevel6<>();
        testPayload.randomize((int) (Math.random() * 100));

        MapClassLevel6<Integer> deserialized = invokeGeneratedCode(javaFile, testPayload);
        assertEquals(testPayload.getStr(), deserialized.getStr());
        assertEquals(testPayload.getStr2(), deserialized.getStr2());
        assertEquals(testPayload, deserialized);

        MapClassLevel6<String> testPayload2 = new MapClassLevel6<>();
        testPayload2.randomize("str" + Math.random() * 100);

        MapClassLevel6<String> deserialized2 = invokeGeneratedCode(javaFile, testPayload2);
        assertEquals(testPayload2.getStr(), deserialized2.getStr());
        assertEquals(testPayload2.getStr2(), deserialized2.getStr2());
        assertEquals(testPayload2, deserialized2);
    }

    @Test
    public void testClassWithNoBeanInfo() throws Exception {
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithNoBeanInfo.class);
        assertNotNull(codeGenSpec);

        ICodeGenerator impl = new CodeGeneratorImpl(PibifyHandlerCacheForTest.class.getCanonicalName());
        JavaFile javaFile = impl.generate(codeGenSpec).getJavaFile();
        assertNotNull(javaFile);
        //javaFile.writeTo(System.out);
        ClassWithNoBeanInfo testPayload = new ClassWithNoBeanInfo();
        testPayload.randomize();

        ClassWithNoBeanInfo deserialized = invokeGeneratedCode(javaFile, testPayload);
        assertEquals(testPayload.str1, deserialized.str1);
        assertEquals(testPayload.getStr2(), deserialized.getStr2());
        assertEquals(testPayload.list, deserialized.list);
        assertEquals(testPayload.enumB, deserialized.enumB);
        assertEquals(testPayload.aMap, deserialized.aMap);
        assertArrayEquals(testPayload.intArray, deserialized.intArray);
        assertEquals(testPayload.bigDecimal, deserialized.bigDecimal);
    }
}