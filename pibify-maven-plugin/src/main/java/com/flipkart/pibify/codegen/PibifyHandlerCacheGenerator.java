package com.flipkart.pibify.codegen;

import com.flipkart.pibify.codegen.stub.AbstractPibifyHandlerCache;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used for generating the PibifyHandler Cache
 * Author bageshwar.pn
 * Date 01/10/24
 */
public class PibifyHandlerCacheGenerator {

    public static final String PIBIFY_HANDLER_CACHE_CLASS_NAME = "PibifyHandlerCache";
    private final Map<Class<?>, ClassName> cache;
    private final String packageName;


    public PibifyHandlerCacheGenerator(String moduleGroupId, String moduleArtifactId) {
        cache = new HashMap<>();
        packageName = getPackageNameFromGroupAndModule(moduleGroupId, moduleArtifactId);
    }


    public PibifyHandlerCacheGenerator(String packageName) {
        cache = new HashMap<>();
        this.packageName = packageName;
    }

    public String getClassName() {
        return packageName + "." + PIBIFY_HANDLER_CACHE_CLASS_NAME;
    }

    public void add(Class<?> clazz, ClassName handler) {
        cache.put(clazz, handler);
    }

    public JavaFileWrapper generateCacheClass() throws CodeGenException {
        if (cache.isEmpty()) {
            throw new IllegalStateException("Generate invoked on empty cache");
        }

        TypeSpec.Builder typeSpecBuilder = getTypeSpecBuilder();
        typeSpecBuilder.addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        TypeSpec pibifyGeneratedHandler = typeSpecBuilder.build();
        JavaFileWrapper wrapper = new JavaFileWrapper();
        wrapper.setPackageName(packageName);
        wrapper.setJavaFile(JavaFile.builder(packageName, pibifyGeneratedHandler)
                .build());
        return wrapper;
    }

    private String getPackageNameFromGroupAndModule(String moduleGroupId, String moduleArtifactId) {
        return moduleGroupId + ".generated." + moduleGroupId;
    }


    private TypeSpec.Builder getTypeSpecBuilder() throws CodeGenException {
        return TypeSpec.classBuilder(PIBIFY_HANDLER_CACHE_CLASS_NAME)
                .addStaticBlock(getStaticBlock())
                .addMethod(getInstanceMethod())
                .addMethod(getConstructor())
                .addField(getInstanceField())
                .superclass(AbstractPibifyHandlerCache.class);
    }

    private CodeBlock getStaticBlock() {
        CodeBlock.Builder staticBlock = CodeBlock.builder();
        staticBlock.addStatement("INSTANCE = new PibifyHandlerCache()");
        return staticBlock.build();
    }

    private MethodSpec getConstructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PROTECTED)
                .addCode(getMapInsertBlock())
                .addStatement("packMap()")
                .build();
    }

    private FieldSpec getInstanceField() {
        return FieldSpec.builder(ClassName.get(packageName, PIBIFY_HANDLER_CACHE_CLASS_NAME),
                        "INSTANCE", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .build();
    }

    private MethodSpec getInstanceMethod() {
        return MethodSpec.methodBuilder("getInstance")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(ClassName.get(packageName, PIBIFY_HANDLER_CACHE_CLASS_NAME))
                .addStatement("return INSTANCE")
                .build();
    }

    private CodeBlock getMapInsertBlock() {
        CodeBlock.Builder mapInsertBlock = CodeBlock.builder();
        for (Map.Entry<Class<?>, ClassName> entry : cache.entrySet()) {
            mapInsertBlock.addStatement("mapBuilder.put($T.class, new $T())", entry.getKey(), entry.getValue());
        }

        return mapInsertBlock.build();
    }
}
