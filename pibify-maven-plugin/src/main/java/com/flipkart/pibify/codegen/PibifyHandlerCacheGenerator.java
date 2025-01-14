package com.flipkart.pibify.codegen;

import com.flipkart.pibify.codegen.stub.AbstractPibifyHandlerCache;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private static String packageCompatibleString(String input) {
        return input
                .replaceAll("-", ".")      // Replace hyphens with dots
                .replaceAll("[^a-zA-Z0-9.]", "")  // Remove any characters that aren't letters, numbers, or dots
                .replaceAll("^[0-9]+|\\.[0-9]+", ""); // Remove leading numbers or numbers after dots
    }

    private String getPackageNameFromGroupAndModule(String moduleGroupId, String moduleArtifactId) {
        return packageCompatibleString(moduleGroupId) + ".pibify.generated." + packageCompatibleString(moduleArtifactId);
    }


    private TypeSpec.Builder getTypeSpecBuilder() throws CodeGenException {
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(PIBIFY_HANDLER_CACHE_CLASS_NAME);
        return classBuilder.addStaticBlock(getStaticBlock())
                .addMethod(getInstanceMethod())
                .addMethod(getConstructor())
                .addMethod(getInsertIntoMapMethod(classBuilder, 0))
                .addField(getInstanceField())
                .superclass(AbstractPibifyHandlerCache.class);
    }

    private CodeBlock getStaticBlock() {
        CodeBlock.Builder staticBlock = CodeBlock.builder();
        staticBlock.addStatement("INSTANCE = new PibifyHandlerCache()");
        staticBlock.addStatement("INSTANCE.initializeHandlers()");
        return staticBlock.build();
    }

    private MethodSpec getConstructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PROTECTED)
                //.addCode(getMapInsertBlock(classBuilder))
                .addStatement("insertIntoMap0()")
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

    private MethodSpec getInsertIntoMapMethod(TypeSpec.Builder classBuilder, int methodIndex) {
        return MethodSpec.methodBuilder("insertIntoMap" + methodIndex)
                .addModifiers(Modifier.PRIVATE)
                .addCode(getMapInsertBlock(classBuilder, methodIndex))
                .build();
    }

    private CodeBlock getMapInsertBlock(TypeSpec.Builder classBuilder, int methodIndex) {
        CodeBlock.Builder mapInsertBlock = CodeBlock.builder();
        List<Map.Entry<Class<?>, ClassName>> entrySet = new ArrayList<>(cache.entrySet());
        for (int j = methodIndex * 1000, i = 0; j < entrySet.size(); j++, i++) {
            Map.Entry<Class<?>, ClassName> entry = entrySet.get(j);
            mapInsertBlock.addStatement("mapBuilder.put($T.class, new $T())", entry.getKey(), entry.getValue());
            i++;
            if (i > 1000) {
                mapInsertBlock.addStatement("insertIntoMap$L()", (methodIndex + 1));
                classBuilder.addMethod(getInsertIntoMapMethod(classBuilder, methodIndex + 1));
                break;
            }
        }

        return mapInsertBlock.build();
    }
}
