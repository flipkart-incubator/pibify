package com.flipkart.pibify.codegen;

/**
 * This class is used for running JMH between object mapper and pibify
 * Author bageshwar.pn
 * Date 11/08/24
 */
@SuppressWarnings("all")
public class PerfTest {

    /*private Object handlerInstance;
    private Method serializeMethod;
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

            compiler.compile(javaFile.toJavaFileObject());
            Class<?> handlerClazz = compiler.loadClass("com.flipkart.pibify.generated."
                    + ClassWithObjectCollections.class.getCanonicalName() + "Handler");
            handlerInstance = handlerClazz.newInstance();
            serializeMethod = handlerClazz.getDeclaredMethod("serialize", ClassWithObjectCollections.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public byte[] pibify() throws Exception {
        ClassWithObjectCollections testPayload = new ClassWithObjectCollections();
        testPayload.randomize();
        return serializeOnly(testPayload);
    }

    ClassWithObjectCollectionsHandler handler = new ClassWithObjectCollectionsHandler();
    public byte[] pibify2() throws Exception {
        ClassWithObjectCollections testPayload = new ClassWithObjectCollections();
        testPayload.randomize();
        return handler.serialize(testPayload);
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

    private <T> byte[] serializeOnly(T data) throws Exception {
        return (byte[]) serializeMethod.invoke(handlerInstance, data);
    }

    @Test
    public void checkBytes() throws Exception {
        ClassWithReferencesToNativeFields object = new ClassWithReferencesToNativeFields().randomize();
        Class<?> handlerClazz = compiler.loadClass("com.flipkart.pibify.generated."
                + ClassWithReferencesToNativeFields.class.getCanonicalName() + "Handler");
        Object o = handlerClazz.newInstance();
        Method serialize = handlerClazz.getDeclaredMethod("serialize", ClassWithReferencesToNativeFields.class);
        byte[] pibify1 = (byte[]) serialize.invoke(o, object);
        byte[] pibify2 = new ClassWithReferencesToNativeFieldsHandler().serialize(object, true);

        byte[] serialize1 = new AnotherClassWithNativeFieldsHandler().serialize(object.getReference());
        System.out.println("hi");
    }

    @Test
    public void measurePibify() throws Exception {
        long start = System.currentTimeMillis();

        int size = 0;
        int loop = 5000;
        for (int i = 0; i < loop; i++) {
            byte[] pibify = pibify();
            size += pibify.length;
        }

        System.out.println(((System.nanoTime() - start) / loop) + "ms for pibify. avg payload " + (size / loop));

        size = 0;
        for (int i = 0; i < loop; i++) {
            byte[] jackson = pibify2();
            size += jackson.length;
        }
        System.out.println(((System.nanoTime() - start) / loop) + "ms for pibify2. avg payload " + (size / loop));


        size = 0;
        for (int i = 0; i < loop; i++) {
            byte[] jackson = jackson();
            size += jackson.length;
        }
        System.out.println(((System.nanoTime() - start) / loop) + "ms for jackson. avg payload " + (size / loop));
    }*/
}
