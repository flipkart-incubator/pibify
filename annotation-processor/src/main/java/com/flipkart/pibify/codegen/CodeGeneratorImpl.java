package com.flipkart.pibify.codegen;

import com.flipkart.pibify.codegen.stub.PibifyGenerated;
import com.flipkart.pibify.serde.IDeserializer;
import com.flipkart.pibify.serde.ISerializer;
import com.flipkart.pibify.serde.PibifyDeserializer;
import com.flipkart.pibify.serde.PibifySerializer;
import com.flipkart.pibify.validation.InvalidPibifyAnnotation;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.flipkart.pibify.codegen.CodeGenUtil.isArray;
import static com.flipkart.pibify.codegen.CodeGenUtil.isCollection;
import static com.flipkart.pibify.codegen.CodeGenUtil.isCollectionOrMap;
import static com.flipkart.pibify.codegen.CodeGenUtil.isNotNative;

/**
 * This class is used for generating the JavaFile given a CodeGenSpec
 * Author bageshwar.pn
 * Date 10/08/24
 */
public class CodeGeneratorImpl implements ICodeGenerator {

    @Override
    public JavaFileWrapper generate(CodeGenSpec codeGenSpec) throws IOException, CodeGenException {

        if (codeGenSpec.getFields().isEmpty()) {
            throw new CodeGenException(codeGenSpec.getPackageName() + "." + codeGenSpec.getClassName() + " does not contain any pibify fields");
        }

        ClassName thePojo = ClassName.get(codeGenSpec.getPackageName(), codeGenSpec.getClassName());
        TypeSpec.Builder typeSpecBuilder = getTypeSpecBuilder(codeGenSpec, thePojo);
        typeSpecBuilder.addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        addInnerClassesForCollectionHandlers(typeSpecBuilder, codeGenSpec);
        TypeSpec pibifyGeneratedHandler = typeSpecBuilder.build();

        String packageName = "com.flipkart.pibify.generated." + codeGenSpec.getPackageName();
        JavaFileWrapper wrapper = new JavaFileWrapper();
        wrapper.setPackageName(packageName);
        wrapper.setJavaFile(JavaFile.builder(packageName, pibifyGeneratedHandler)
                .build());
        return wrapper;
    }

    private TypeSpec.Builder getTypeSpecBuilder(CodeGenSpec codeGenSpec, ClassName thePojo) throws CodeGenException {
        return TypeSpec.classBuilder(codeGenSpec.getClassName() + "Handler")
                .addMethod(getSerializer(thePojo, codeGenSpec))
                .addMethod(getDeserializer(thePojo, codeGenSpec))
                .superclass(ParameterizedTypeName.get(ClassName.get(PibifyGenerated.class), thePojo));
    }

    private void addHandlerBasedOnDatatype(CodeGenSpec.Type fieldSpec, MethodSpec.Builder builder) {
        if (fieldSpec.getNativeType() == CodeGenSpec.DataType.COLLECTION) {
            addHandlerBasedOnDatatypeImpl(fieldSpec, 0, "value", builder);
        } else if (fieldSpec.getNativeType() == CodeGenSpec.DataType.MAP) {
            addHandlerBasedOnDatatypeImpl(fieldSpec, 0, "key", builder);
            addHandlerBasedOnDatatypeImpl(fieldSpec, 1, "value", builder);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private void addHandlerBasedOnDatatypeImpl(CodeGenSpec.Type fieldSpec, int index, String value, MethodSpec.Builder builder) {
        if (CodeGenUtil.isNotNative(fieldSpec.getContainerTypes().get(index).getNativeType())) {
            if (CodeGenUtil.isObject(fieldSpec.getContainerTypes().get(index).getNativeType())) {
                addHandlerForObjectReference(value, builder, fieldSpec.getContainerTypes().get(index).getReferenceType());
            } else {
                builder.addStatement("$T<$L> $LHandler = HANDLER_MAP.get($S)", PibifyGenerated.class,
                        fieldSpec.getContainerTypes().get(index).getGenericTypeSignature(), value,
                        fieldSpec.getContainerTypes().get(index).getGenericTypeSignature());
            }
        } else {
            // directly use natives
        }
    }

    private void addInnerClassesForCollectionHandlers(TypeSpec.Builder typeSpecBuilder, CodeGenSpec codeGenSpec) throws CodeGenException {
        AtomicInteger counter = new AtomicInteger(1);
        Map<String, String> mapOfGenericSignatureToHandlerName = new HashMap<>();
        for (CodeGenSpec.FieldSpec fieldSpec : codeGenSpec.getFields()) {
            if (isCollectionOrMap(fieldSpec.getType().getNativeType())) {
                addInnerClassesForCollectionHandlersImpl(typeSpecBuilder, fieldSpec.getType(), counter, mapOfGenericSignatureToHandlerName);
            }
        }

        // TODO instead of using the map in generated code, use the map in this layer of the code
        CodeBlock.Builder staticBlockBuilder = CodeBlock.builder();
        staticBlockBuilder.addStatement("HANDLER_MAP = new $T<>()", HashMap.class);
        for (Map.Entry<String, String> entry : mapOfGenericSignatureToHandlerName.entrySet()) {
            staticBlockBuilder.addStatement("HANDLER_MAP.put($S, new $T())", entry.getKey(),
                    ClassName.bestGuess(entry.getValue()));
        }

        typeSpecBuilder.addField(ParameterizedTypeName.get(Map.class, String.class, PibifyGenerated.class),
                        "HANDLER_MAP", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .addStaticBlock(staticBlockBuilder.build());

    }

    private void addInnerClassesForCollectionHandlersImpl(TypeSpec.Builder typeSpecBuilder, CodeGenSpec.Type fieldSpec,
                                                          AtomicInteger counter, Map<String, String> mapOfGenericSignatureToHandlerName) throws CodeGenException {
        // pass the field name and use fieldName + Handler + (counter++) to have names of the handlers
        String className = "InternalHandler" + counter.incrementAndGet();
        typeSpecBuilder.addType(
                TypeSpec.classBuilder(className)
                        .addModifiers(Modifier.STATIC)
                        .addMethod(getSerializerForCollectionHandler(fieldSpec))
                        .addMethod(getDeserializerForCollectionHandler(fieldSpec))
                        .superclass(ParameterizedTypeName.get(ClassName.get(PibifyGenerated.class), fieldSpec.getjPTypeName()))
                        .build()
        );

        mapOfGenericSignatureToHandlerName.put(fieldSpec.getGenericTypeSignature(), className);

        if (fieldSpec.getNativeType() == CodeGenSpec.DataType.COLLECTION
                && isCollectionOrMap(fieldSpec.getContainerTypes().get(0).getNativeType())) {
            addInnerClassesForCollectionHandlersImpl(typeSpecBuilder, fieldSpec.getContainerTypes().get(0),
                    counter, mapOfGenericSignatureToHandlerName);
        } else if (fieldSpec.getNativeType() == CodeGenSpec.DataType.MAP) {
            if (isCollectionOrMap(fieldSpec.getContainerTypes().get(0).getNativeType())) {
                addInnerClassesForCollectionHandlersImpl(typeSpecBuilder,
                        fieldSpec.getContainerTypes().get(0), counter, mapOfGenericSignatureToHandlerName);
            }

            if (isCollectionOrMap(fieldSpec.getContainerTypes().get(1).getNativeType())) {
                addInnerClassesForCollectionHandlersImpl(typeSpecBuilder,
                        fieldSpec.getContainerTypes().get(1), counter, mapOfGenericSignatureToHandlerName);
            }
        }
    }

    private void addHandlerForObjectReference(CodeGenSpec.FieldSpec fieldSpec, MethodSpec.Builder builder, CodeGenSpec codeGenSpec) {
        addHandlerForObjectReference(fieldSpec.getName(), builder, codeGenSpec);
    }

    private static void addWriterBlockForCollectionHandler(CodeGenSpec.Type fieldSpec, MethodSpec.Builder builder, int index, String value, int writeIndex, String handler) {
        if (CodeGenUtil.isNotNative(fieldSpec.getContainerTypes().get(index).getNativeType())) {
            builder.addStatement("serializer.writeObjectAsBytes($L, $L.serialize($L))", writeIndex, handler, value);
        } else {
            builder.addStatement("serializer.write$L($L, $L)",
                    fieldSpec.getContainerTypes().get(index).getNativeType().getReadWriteMethodName(), writeIndex, value);
        }
    }

    private MethodSpec getSerializerForCollectionHandler(CodeGenSpec.Type fieldSpec) throws CodeGenException {
        try {
            MethodSpec.Builder builder = MethodSpec.methodBuilder("serialize")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .returns(byte[].class)
                    .addException(PibifyCodeExecException.class)
                    .addParameter(fieldSpec.getjPTypeName(), "object")
                    .beginControlFlow("if (object == null)")
                    .addStatement("return null")
                    .endControlFlow()
                    .addStatement("$T serializer = new $T()", ISerializer.class, PibifySerializer.class);

            addHandlerBasedOnDatatype(fieldSpec, builder);

            builder.beginControlFlow("try");
            String signature = fieldSpec.getContainerTypes().get(0).getGenericTypeSignature();
            if (fieldSpec.getNativeType() == CodeGenSpec.DataType.COLLECTION) {
                builder.beginControlFlow("for ($L value : object)", signature);

                addWriterBlockForCollectionHandler(fieldSpec, builder, 0, "value", 1, "valueHandler");
            } else if (fieldSpec.getNativeType() == CodeGenSpec.DataType.MAP) {
                builder.beginControlFlow("for (java.util.Map.Entry<$L, $L> entry : object.entrySet())",
                        signature, fieldSpec.getContainerTypes().get(1).getGenericTypeSignature());

                addWriterBlockForCollectionHandler(fieldSpec, builder, 0, "entry.getKey()", 1, "keyHandler");
                addWriterBlockForCollectionHandler(fieldSpec, builder, 1, "entry.getValue()", 2, "valueHandler");
            } else {
                throw new UnsupportedOperationException();
            }

            builder.endControlFlow();

            builder.addStatement("return serializer.serialize()")
                    .nextControlFlow("catch ($T e)", Exception.class)
                    .addStatement("throw new $T(e)", PibifyCodeExecException.class)
                    .endControlFlow();

            return builder.build();
        } catch (Exception e) {
            throw new CodeGenException(e.getMessage(), e);
        }
    }

    private MethodSpec getSerializer(ClassName thePojo, CodeGenSpec codeGenSpec) throws CodeGenException {

        try {
            MethodSpec.Builder builder = MethodSpec.methodBuilder("serialize")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .returns(byte[].class)
                    .addException(PibifyCodeExecException.class)
                    .addParameter(thePojo, "object")
                    .beginControlFlow("if (object == null)")
                    .addStatement("return null")
                    .endControlFlow()
                    .addStatement("$T serializer = new $T()", ISerializer.class, PibifySerializer.class)
                    .beginControlFlow("try");

            for (CodeGenSpec.FieldSpec fieldSpec : codeGenSpec.getFields()) {
                switch (fieldSpec.getType().getNativeType()) {
                    case ARRAY:
                    case COLLECTION:
                        generateSerializerForArrayOrCollection(fieldSpec, builder);
                        break;
                    case MAP:
                        generateSerializerForMap(fieldSpec, builder);
                        break;
                    case OBJECT:
                        generateSerializerForObjectReference(fieldSpec, builder);
                        break;
                    case ENUM:
                        builder.addStatement("serializer.writeEnum($L, object.$L())", fieldSpec.getIndex(), fieldSpec.getGetter());
                        break;
                    default:
                        builder.addStatement("serializer.write$L($L, object.$L())",
                                fieldSpec.getType().getNativeType().getReadWriteMethodName(), fieldSpec.getIndex(), fieldSpec.getGetter());
                }
            }

            builder.addStatement("return serializer.serialize()")
                    .nextControlFlow("catch ($T e)", Exception.class)
                    .addStatement("throw new $T(e)", PibifyCodeExecException.class)
                    .endControlFlow();

            return builder.build();
        } catch (Exception e) {
            throw new CodeGenException(e.getMessage(), e);
        }
    }

    private void addHandlerForObjectReference(String name, MethodSpec.Builder builder, CodeGenSpec codeGenSpec) {
        builder.addStatement("$T $LHandler = new $T()",
                ParameterizedTypeName.get(ClassName.get(PibifyGenerated.class),
                        ClassName.get(codeGenSpec.getPackageName(), codeGenSpec.getClassName())
                ), name,
                ClassName.get("com.flipkart.pibify.generated." + codeGenSpec.getPackageName(),
                        codeGenSpec.getClassName() + "Handler"));
    }

    private MethodSpec getDeserializerForCollectionHandler(CodeGenSpec.Type fieldSpec) throws CodeGenException {
        try {
            MethodSpec.Builder builder = MethodSpec.methodBuilder("deserialize")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .returns(fieldSpec.getjPTypeName())
                    .addException(PibifyCodeExecException.class)
                    .addParameter(byte[].class, "bytes")
                    .beginControlFlow("try")

                    .addStatement("$T deserializer = new $T(bytes)", IDeserializer.class, PibifyDeserializer.class)
                    .addStatement("int tag = deserializer.getNextTag()");

            if (fieldSpec.getNativeType() == CodeGenSpec.DataType.COLLECTION) {
                builder.addStatement("$T object = new $T()", fieldSpec.getjPTypeName(), fieldSpec.getCollectionType().getImplementationClass());
            } else if (fieldSpec.getNativeType() == CodeGenSpec.DataType.MAP) {
                builder.addStatement("$T object = new $T()", fieldSpec.getjPTypeName(), HashMap.class);
            } else {
                throw new UnsupportedOperationException();
            }

            addHandlerBasedOnDatatype(fieldSpec, builder);

            if (fieldSpec.getNativeType() == CodeGenSpec.DataType.COLLECTION) {
                builder.addStatement("$T value", fieldSpec.getContainerTypes().get(0).getjPTypeName());
                builder.beginControlFlow("while (tag != 0) ");

                if (isNotNative(fieldSpec.getContainerTypes().get(0).getNativeType())) {
                    //value = refHandler.deserialize(deserializer.readObjectAsBytes());
                    builder.addStatement("value = valueHandler.deserialize(deserializer.readObjectAsBytes())");
                } else {
                    //value = deserializer.readString();
                    if (fieldSpec.getContainerTypes().get(0).getNativeType() == CodeGenSpec.DataType.ENUM) {
                        builder.addStatement("value = $T.values()[deserializer.readEnum()]",
                                fieldSpec.getContainerTypes().get(0).getjPTypeName());
                    } else {
                        builder.addStatement("value = deserializer.read$L()", fieldSpec.getContainerTypes().get(0).getNativeType().getReadWriteMethodName());
                    }
                }

                //collection.add(value);
                builder.addStatement("object.add(value)");
            } else if (fieldSpec.getNativeType() == CodeGenSpec.DataType.MAP) {
                builder.addStatement("$T key", fieldSpec.getContainerTypes().get(0).getjPTypeName());
                builder.addStatement("$T value", fieldSpec.getContainerTypes().get(1).getjPTypeName());
                builder.beginControlFlow("while (tag != 0) ");

                if (isNotNative(fieldSpec.getContainerTypes().get(0).getNativeType())) {
                    builder.addStatement("key = keyHandler.deserialize(deserializer.readObjectAsBytes())");
                } else {
                    if (fieldSpec.getContainerTypes().get(0).getNativeType() == CodeGenSpec.DataType.ENUM) {
                        builder.addStatement("key = $T.values()[deserializer.readEnum()]",
                                fieldSpec.getContainerTypes().get(0).getjPTypeName());
                    } else {
                        builder.addStatement("key = deserializer.read$L()",
                                fieldSpec.getContainerTypes().get(0).getNativeType().getReadWriteMethodName());
                    }
                }
                builder.addStatement("tag = deserializer.getNextTag()");

                if (isNotNative(fieldSpec.getContainerTypes().get(1).getNativeType())) {
                    builder.addStatement("value = valueHandler.deserialize(deserializer.readObjectAsBytes())");
                } else {
                    if (fieldSpec.getContainerTypes().get(1).getNativeType() == CodeGenSpec.DataType.ENUM) {
                        builder.addStatement("value = $T.values()[deserializer.readEnum()]",
                                fieldSpec.getContainerTypes().get(1).getjPTypeName());
                    } else {
                        builder.addStatement("value = deserializer.read$L()",
                                fieldSpec.getContainerTypes().get(1).getNativeType().getReadWriteMethodName());
                    }
                }
                //object.put(key, value);
                builder.addStatement("object.put(key, value)");
            } else {
                throw new UnsupportedOperationException();
            }


            builder.addStatement("tag = deserializer.getNextTag()")
                    .endControlFlow()
                    .addStatement("return object")
                    .nextControlFlow("catch ($T e)", Exception.class)
                    .addStatement("throw new $T(e)", PibifyCodeExecException.class)
                    .endControlFlow();

            return builder.build();
        } catch (Exception e) {
            throw new CodeGenException(e.getMessage(), e);
        }
    }

    private void generateSerializerForArrayOrCollection(CodeGenSpec.FieldSpec fieldSpec, MethodSpec.Builder builder) {
        if (!(CodeGenUtil.isArray(fieldSpec) || CodeGenUtil.isCollection(fieldSpec))) {
            throw new IllegalArgumentException("generateSerializerForArrayOrCollection invoked for non-array types " + fieldSpec.getName());
        }

        CodeGenSpec.Type realizedType = fieldSpec.getType().getContainerTypes().get(0);
        if (realizedType.getNativeType() == CodeGenSpec.DataType.UNKNOWN) {
            // TODO  unknowns to be handled as a byte[] via object mapper.
            //
            throw new UnsupportedOperationException("Unknown arrays/collections not supported right now");
        } else {

            if (isArray(fieldSpec) && realizedType.getNativeType() == CodeGenSpec.DataType.OBJECT) {
                addHandlerForObjectReference(fieldSpec, builder, realizedType.getReferenceType());
            }

            if (isCollection(fieldSpec)) {
                // PibifyGenerated<List<List<String>>> handler = new InternalHandler2();
                builder.addStatement("PibifyGenerated<$L> $LHandler = HANDLER_MAP.get($S)",
                        fieldSpec.getType().getGenericTypeSignature(), fieldSpec.getName(), fieldSpec.getType().getGenericTypeSignature());

                // handler.serialize(object.getaString());
                builder.addStatement("serializer.writeObjectAsBytes($L, $LHandler.serialize(object.$L()))",
                        fieldSpec.getIndex(), fieldSpec.getName(), fieldSpec.getGetter());
            } else if (isArray(fieldSpec)) {
                builder.beginControlFlow("for ($T val : object.$L())",
                        getReferenceTypeForContainers(realizedType, false), fieldSpec.getGetter());

                if (realizedType.getNativeType() == CodeGenSpec.DataType.OBJECT) {
                    builder.addStatement("serializer.writeObjectAsBytes($L, $LHandler.serialize(val))",
                            fieldSpec.getIndex(), fieldSpec.getName());
                } else {
                    builder.addStatement("serializer.write$L($L, val)",
                            realizedType.getNativeType().getReadWriteMethodName(), fieldSpec.getIndex());
                }

                builder.endControlFlow();
            } else {
                throw new UnsupportedOperationException();
            }
        }
    }

    // Can return a ClassName of a Class (for java natives)
    // @returns Either the javapoet class name, or java reflection class

    private Object getReferenceTypeForContainers(CodeGenSpec.Type realizedType, boolean preferAutoboxed) {
        if (realizedType.getNativeType() == CodeGenSpec.DataType.OBJECT
                || realizedType.getNativeType() == CodeGenSpec.DataType.ENUM) {
            return ClassName.get(realizedType.getReferenceType().getPackageName(), realizedType.getReferenceType().getClassName());
        } else {

            // if native type is collection or map
            // get deep name, else return native types clazz
            if (realizedType.getNativeType() == CodeGenSpec.DataType.COLLECTION) {
                return Collection.class;
            } else if (realizedType.getNativeType() == CodeGenSpec.DataType.MAP) {
                return Map.class;
            } else {
                if (preferAutoboxed) {
                    return ClassName.get(realizedType.getNativeType().getAutoboxedClass());
                } else {
                    return realizedType.getNativeType().getClazz();
                }
            }
        }
    }

    private void generateSerializerForMap(CodeGenSpec.FieldSpec fieldSpec, MethodSpec.Builder builder) {

        if (fieldSpec.getType().getNativeType() != CodeGenSpec.DataType.MAP) {
            throw new IllegalArgumentException("generateSerializerForMap invoked for non-map types " + fieldSpec.getName());
        }

        CodeGenSpec.Type keyType = fieldSpec.getType().getContainerTypes().get(0);
        CodeGenSpec.Type valueType = fieldSpec.getType().getContainerTypes().get(1);
        if (keyType.getNativeType() == CodeGenSpec.DataType.UNKNOWN
                || valueType.getNativeType() == CodeGenSpec.DataType.UNKNOWN) {
            throw new UnsupportedOperationException("Unknown k/v in maps not supported right now");
        } else {

            /*if (keyType.getNativeType() == CodeGenSpec.DataType.OBJECT) {
                CodeGenSpec refSpec = keyType.getReferenceType();
                builder.addStatement("$T key$LHandler = new $T()",
                        ParameterizedTypeName.get(ClassName.get(PibifyGenerated.class),
                                ClassName.get(refSpec.getPackageName(), refSpec.getClassName())
                        ), fieldSpec.getName(),
                        ClassName.get("com.flipkart.pibify.generated." + refSpec.getPackageName(),
                                refSpec.getClassName() + "Handler"));
            }

            if (valueType.getNativeType() == CodeGenSpec.DataType.OBJECT) {
                CodeGenSpec refSpec = valueType.getReferenceType();
                builder.addStatement("$T value$LHandler = new $T()",
                        ParameterizedTypeName.get(ClassName.get(PibifyGenerated.class),
                                ClassName.get(refSpec.getPackageName(), refSpec.getClassName())
                        ), fieldSpec.getName(),
                        ClassName.get("com.flipkart.pibify.generated." + refSpec.getPackageName(),
                                refSpec.getClassName() + "Handler"));
            }*/

            // PibifyGenerated<List<List<String>>> handler = new InternalHandler2();
            builder.addStatement("PibifyGenerated<$L> $LHandler = HANDLER_MAP.get($S)",
                    fieldSpec.getType().getGenericTypeSignature(), fieldSpec.getName(), fieldSpec.getType().getGenericTypeSignature());

            // serializer.writeObjectAsBytes(1, handler.serialize(object.getaString()));
            builder.addStatement("serializer.writeObjectAsBytes($L, $LHandler.serialize(object.$L()))", fieldSpec.getIndex(),
                    fieldSpec.getName(), fieldSpec.getGetter());


            builder.addStatement("/*");
            builder.beginControlFlow("for ($T<$L, $L> entry : object.$L().entrySet())",
                            Map.Entry.class,
                            getReferenceTypeForContainers(keyType, true),
                            getReferenceTypeForContainers(valueType, true),
                            fieldSpec.getGetter())
                    .addStatement("$T $LSerializer = new $T()", ISerializer.class, fieldSpec.getName(),
                            PibifySerializer.class);

            if (keyType.getNativeType() == CodeGenSpec.DataType.OBJECT) {
                builder.addStatement("$LSerializer.writeObjectAsBytes(1, key$LHandler.serialize(entry.getKey()))", fieldSpec.getName(), fieldSpec.getName());
            } else {
                builder.addStatement("$LSerializer.write$L(1, entry.getKey())", fieldSpec.getName(), keyType.getNativeType().getReadWriteMethodName());
            }

            if (valueType.getNativeType() == CodeGenSpec.DataType.OBJECT) {
                builder.addStatement("$LSerializer.writeObjectAsBytes(2, value$LHandler.serialize(entry.getValue()))", fieldSpec.getName(), fieldSpec.getName());
            } else {
                builder.addStatement("$LSerializer.write$L(2, entry.getValue())", fieldSpec.getName(), valueType.getNativeType().getReadWriteMethodName());
            }

            builder.addStatement("serializer.writeObjectAsBytes($L, $LSerializer.serialize())", fieldSpec.getIndex(), fieldSpec.getName())
                    .endControlFlow();
            builder.addStatement("*/");
        }
    }

    private void generateSerializerForObjectReference(CodeGenSpec.FieldSpec fieldSpec, MethodSpec.Builder builder) {
        if (fieldSpec.getType().getNativeType() != CodeGenSpec.DataType.OBJECT) {
            throw new IllegalArgumentException("generateSerializerForObjectReference invoked for non-reference types "
                    + fieldSpec.getName());
        }

        addHandlerForObjectReference(fieldSpec, builder, fieldSpec.getType().getReferenceType());
        builder.addStatement("serializer.writeObjectAsBytes($L, $LHandler.serialize(object.$L()))", fieldSpec.getIndex(),
                fieldSpec.getName(), fieldSpec.getGetter());
    }

    private MethodSpec getDeserializer(ClassName thePojo, CodeGenSpec codeGenSpec) throws CodeGenException {
        try {
            MethodSpec.Builder builder = MethodSpec.methodBuilder("deserialize")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .returns(thePojo)
                    .addException(PibifyCodeExecException.class)
                    .addParameter(byte[].class, "bytes")
                    .beginControlFlow("try")
                    .addStatement("$T object = new $T()", thePojo, thePojo)
                    .addStatement("$T deserializer = new $T(bytes)", IDeserializer.class, PibifyDeserializer.class)
                    .addStatement("int tag = deserializer.getNextTag()")
                    .beginControlFlow("while (tag != 0) ")
                    .beginControlFlow("switch (tag) ");

            for (CodeGenSpec.FieldSpec fieldSpec : codeGenSpec.getFields()) {
                addFieldDeserializerBlock(fieldSpec, builder);
                builder.addStatement("$>$> break$<$<");
            }

            builder.addStatement("default: throw new UnsupportedOperationException($S)", "Unable to find tag in gen code")
                    .endControlFlow()
                    .addStatement("tag = deserializer.getNextTag()")
                    .endControlFlow()
                    .addStatement("return object")
                    .nextControlFlow("catch ($T e)", Exception.class)
                    .addStatement("throw new $T(e)", PibifyCodeExecException.class)
                    .endControlFlow();

            return builder.build();
        } catch (Exception e) {
            throw new CodeGenException(e.getMessage(), e);
        }
    }

    private void addFieldDeserializerBlock(CodeGenSpec.FieldSpec fieldSpec, MethodSpec.Builder builder) throws InvalidPibifyAnnotation {
        switch (fieldSpec.getType().getNativeType()) {
            case ARRAY:
                addArrayDeserializer(fieldSpec, builder);
                break;
            case COLLECTION:
                addCollectionDeserializer(fieldSpec, builder);
                break;
            case MAP:
                addMapDeserializer(fieldSpec, builder);
                break;
            case OBJECT:
                addObjectDeserializer(fieldSpec, builder);
                break;
            default:
                String enumBlock = "", enumEndBlock = "";
                if (fieldSpec.getType().getNativeType() == CodeGenSpec.DataType.ENUM) {
                    enumBlock = fieldSpec.getType().getReferenceType().getPackageName() + "."
                            + fieldSpec.getType().getReferenceType().getClassName() + ".values()[";
                    enumEndBlock = "]";
                }

                builder.addStatement("case $L: \n $>" +
                                "object.$L($L$L deserializer.read$L()$L)$<",
                        TagPredictor.getTagBasedOnField(fieldSpec.getIndex(), getClassForTag(fieldSpec)),
                        fieldSpec.getSetter(),
                        enumBlock,
                        getCastIfRequired(fieldSpec.getType().getNativeType()),
                        fieldSpec.getType().getNativeType().getReadWriteMethodName(),
                        enumEndBlock
                );
                break;
        }
    }

    private void addMapDeserializer(CodeGenSpec.FieldSpec fieldSpec, MethodSpec.Builder builder) throws InvalidPibifyAnnotation {
        CodeGenSpec.Type keyType = fieldSpec.getType().getContainerTypes().get(0);
        CodeGenSpec.Type valueType = fieldSpec.getType().getContainerTypes().get(1);

        int tag = TagPredictor.getTagBasedOnField(fieldSpec.getIndex(), byte[].class);

        //PibifyGenerated<java.util.List<java.util.List<java.lang.String>>> aStringHandler = HANDLER_MAP.get("java.util.List<java.util.List<java.lang.String>>");
        builder.addStatement("case $L: \n$>" +
                        "$T<$L> $LHandler = HANDLER_MAP.get($S)", tag, PibifyGenerated.class,
                fieldSpec.getType().getGenericTypeSignature(),
                fieldSpec.getName(), fieldSpec.getType().getGenericTypeSignature());

        //object.setaString(aStringHandler.deserialize(deserializer.readObjectAsBytes()));
        builder.addStatement("object.$L($LHandler.deserialize(deserializer.readObjectAsBytes()))", fieldSpec.getSetter(), fieldSpec.getName());

        builder.addStatement("/*");
        builder.addStatement("case $L: \n$>" +
                        "byte[] bytes$L = deserializer.readObjectAsBytes()", tag, tag)
                .beginControlFlow("if (object.$L() == null)", fieldSpec.getGetter())
                .addStatement("object.$L(new $T())", fieldSpec.getSetter(), HashMap.class)
                .endControlFlow()
                .addStatement("$T deserializer$L = new $T(bytes$L)", IDeserializer.class, tag, PibifyDeserializer.class, tag)
                .addStatement("int tag$L = deserializer$L.getNextTag()", tag, tag);

        if (keyType.getNativeType() == CodeGenSpec.DataType.OBJECT) {
            CodeGenSpec refSpec = keyType.getReferenceType();
            builder.addStatement("$T key$LHandler = new $T()",
                    ParameterizedTypeName.get(ClassName.get(PibifyGenerated.class),
                            ClassName.get(refSpec.getPackageName(), refSpec.getClassName())
                    ), fieldSpec.getName(),
                    ClassName.get("com.flipkart.pibify.generated." + refSpec.getPackageName(),
                            refSpec.getClassName() + "Handler"));

            builder.addStatement("$T key = key$LHandler.deserialize(deserializer$L.read$L())",
                    getReferenceTypeForContainers(keyType, true), fieldSpec.getName(), tag,
                    keyType.getNativeType().getReadWriteMethodName());
        } else {
            builder.addStatement("$T key = deserializer$L.read$L()", getReferenceTypeForContainers(keyType, true), tag, keyType.getNativeType().getReadWriteMethodName());
        }

        builder.addStatement("tag$L = deserializer$L.getNextTag()", tag, tag);

        if (valueType.getNativeType() == CodeGenSpec.DataType.OBJECT) {

            CodeGenSpec refSpec = valueType.getReferenceType();
            builder.addStatement("$T value$LHandler = new $T()",
                    ParameterizedTypeName.get(ClassName.get(PibifyGenerated.class),
                            ClassName.get(refSpec.getPackageName(), refSpec.getClassName())
                    ), fieldSpec.getName(),
                    ClassName.get("com.flipkart.pibify.generated." + refSpec.getPackageName(),
                            refSpec.getClassName() + "Handler"));

            builder.addStatement("$T value = value$LHandler.deserialize(deserializer$L.read$L())",
                    getReferenceTypeForContainers(valueType, true), fieldSpec.getName(), tag,
                    valueType.getNativeType().getReadWriteMethodName());
        } else {
            builder.addStatement("$T value = deserializer$L.read$L()", getReferenceTypeForContainers(valueType, true), tag, valueType.getNativeType().getReadWriteMethodName());
        }

        builder.addStatement("object.$L().put(key, value)", fieldSpec.getGetter());
        builder.addStatement("*/");
    }

    private void addCollectionDeserializer(CodeGenSpec.FieldSpec fieldSpec, MethodSpec.Builder builder) throws InvalidPibifyAnnotation {

        CodeGenSpec.Type realizedType = fieldSpec.getType().getContainerTypes().get(0);
        int tag = TagPredictor.getTagBasedOnField(fieldSpec.getIndex(), byte[].class);

        //PibifyGenerated<java.util.List<java.util.List<java.lang.String>>> aStringHandler = HANDLER_MAP.get("java.util.List<java.util.List<java.lang.String>>");
        builder.addStatement("case $L: \n$>" +
                        "$T<$L> $LHandler = HANDLER_MAP.get($S)", tag, PibifyGenerated.class,
                fieldSpec.getType().getGenericTypeSignature(),
                fieldSpec.getName(), fieldSpec.getType().getGenericTypeSignature());

        //object.setaString(aStringHandler.deserialize(deserializer.readObjectAsBytes()));
        builder.addStatement("object.$L($LHandler.deserialize(deserializer.readObjectAsBytes()))", fieldSpec.getSetter(), fieldSpec.getName());

        builder.addStatement("/*");
        if (realizedType.getNativeType() == CodeGenSpec.DataType.OBJECT) {

            CodeGenSpec refSpec = realizedType.getReferenceType();
            builder.addStatement("case $L: \n$>" +
                            "$T $LHandler = new $T()", tag,
                    ParameterizedTypeName.get(ClassName.get(PibifyGenerated.class),
                            ClassName.get(refSpec.getPackageName(), refSpec.getClassName())
                    ), fieldSpec.getName(),
                    ClassName.get("com.flipkart.pibify.generated." + refSpec.getPackageName(),
                            refSpec.getClassName() + "Handler"));

            builder.addStatement("$>$T val$L = $LHandler.deserialize(deserializer.read$L())",
                    getReferenceTypeForContainers(fieldSpec.getType().getContainerTypes().get(0), false), tag,
                    fieldSpec.getName(), realizedType.getNativeType().getReadWriteMethodName());
        } else {
            builder.addStatement("case $L:\n$T val$L = deserializer.read$L()", tag,
                    getReferenceTypeForContainers(fieldSpec.getType().getContainerTypes().get(0), false), tag,
                    realizedType.getNativeType().getReadWriteMethodName());
        }


        builder.beginControlFlow("$>if (object.$L() == null)$<", fieldSpec.getGetter())
                .addStatement("$>object.$L(new $T())$<", fieldSpec.getSetter(), fieldSpec.getType().getCollectionType().getImplementationClass())
                .endControlFlow()
                .addStatement("$>object.$L().add(val$L)$<", fieldSpec.getGetter(), tag);

        builder.addStatement("*/");
    }

    private void addArrayDeserializer(CodeGenSpec.FieldSpec fieldSpec, MethodSpec.Builder builder) throws InvalidPibifyAnnotation {
        CodeGenSpec.Type realizedType = fieldSpec.getType().getContainerTypes().get(0);

        if (realizedType.getNativeType() == CodeGenSpec.DataType.UNKNOWN) return;

        int tag = TagPredictor.getTagBasedOnField(fieldSpec.getIndex(), getClassForTag(fieldSpec));
        Object typeForContainers = getReferenceTypeForContainers(fieldSpec.getType().getContainerTypes().get(0), false);
        builder.addStatement("case $L: \n$>$T[] newArray$L", tag, typeForContainers, tag)
                .addStatement("$>$T[] oldArray$L = object.$L()$<", typeForContainers, tag, fieldSpec.getGetter());
        if (realizedType.getNativeType() == CodeGenSpec.DataType.OBJECT) {
            CodeGenSpec refSpec = realizedType.getReferenceType();
            addHandlerForObjectReference(fieldSpec, builder, refSpec);

            builder.addStatement("$>$T val$L = $LHandler.deserialize(deserializer.read$L())",
                    getReferenceTypeForContainers(fieldSpec.getType().getContainerTypes().get(0), false), tag,
                    fieldSpec.getName(), realizedType.getNativeType().getReadWriteMethodName());
        } else {
            if (realizedType.getNativeType() == CodeGenSpec.DataType.ENUM) {
                builder.addStatement("$>$T val$L = $T.values()[deserializer.readEnum()]$<",
                        typeForContainers, tag, typeForContainers);
            } else {
                builder.addStatement("$>$T val$L = deserializer.read$L()$<",
                        typeForContainers, tag, realizedType.getNativeType().getReadWriteMethodName());
            }
        }

        builder.beginControlFlow("$>if (oldArray$L == null)$<", tag)
                .addStatement("$>newArray$L = new $T[]{val$L}$<", tag, typeForContainers, tag)
                .endControlFlow()
                .beginControlFlow("$>else$<")
                .addStatement("$>newArray$L = $T.copyOf(oldArray$L, oldArray$L.length + 1)$<", tag, Arrays.class, tag, tag)
                .addStatement("$>newArray$L[oldArray$L.length] = val$L$<", tag, tag, tag)
                .endControlFlow()
                .addStatement("$>object.$L(newArray$L)$<", fieldSpec.getSetter(), tag);
    }

    private void addObjectDeserializer(CodeGenSpec.FieldSpec fieldSpec, MethodSpec.Builder builder) throws InvalidPibifyAnnotation {
        CodeGenSpec refSpec = fieldSpec.getType().getReferenceType();
        int tag = TagPredictor.getTagBasedOnField(fieldSpec.getIndex(), byte[].class);

        builder.addStatement("case $L: \n$>$T $LHandler$L = new $T()", tag,
                ParameterizedTypeName.get(ClassName.get(PibifyGenerated.class),
                        ClassName.get(refSpec.getPackageName(), refSpec.getClassName())
                ), fieldSpec.getName(), tag,
                ClassName.get("com.flipkart.pibify.generated." + refSpec.getPackageName(),
                        refSpec.getClassName() + "Handler"));

        builder.addStatement("$>object.$L($LHandler$L.deserialize(deserializer.readObjectAsBytes()))$<", fieldSpec.getSetter(), fieldSpec.getName(), tag);

    }

    private Class<?> getClassForTag(CodeGenSpec.FieldSpec fieldSpec) {
        switch (fieldSpec.getType().getNativeType()) {
            case ARRAY:
            case COLLECTION:
                CodeGenSpec.DataType containerNativeType = fieldSpec.getType().getContainerTypes().get(0).getNativeType();
                if (containerNativeType == CodeGenSpec.DataType.OBJECT
                        || containerNativeType == CodeGenSpec.DataType.COLLECTION
                        || containerNativeType == CodeGenSpec.DataType.MAP) {
                    return Object.class;
                } else {
                    return fieldSpec.getType().getContainerTypes().get(0).getNativeType().getClazz();
                }

            case OBJECT:
                return Object.class;
            default:
                return fieldSpec.getType().getNativeType().getClazz();
        }
    }

    private String getCastIfRequired(CodeGenSpec.DataType nativeType) {
        switch (nativeType) {
            case CHAR:
                return "(char)";
            case BYTE:
                return "(byte)";
            case SHORT:
                return "(short)";
            default:
                return "";
        }
    }
}
