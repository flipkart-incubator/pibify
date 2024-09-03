package com.flipkart.pibify.codegen;

import com.flipkart.pibify.codegen.stub.PibifyGenerated;
import com.flipkart.pibify.serde.IDeserializer;
import com.flipkart.pibify.serde.ISerializer;
import com.flipkart.pibify.serde.PibifyDeserializer;
import com.flipkart.pibify.serde.PibifySerializer;
import com.flipkart.pibify.validation.InvalidPibifyAnnotation;
import com.squareup.javapoet.ClassName;
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

import static com.flipkart.pibify.codegen.CodeGenUtil.isCollectionOrMap;

/**
 * This class is used for generating the JavaFile given a CodeGenSpec
 * Author bageshwar.pn
 * Date 10/08/24
 */
public class CodeGeneratorImpl implements ICodeGenerator {

    @Override
    public JavaFileWrapper generate(CodeGenSpec codeGenSpec) throws IOException, CodeGenException {

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

    private void addInnerClassesForCollectionHandlers(TypeSpec.Builder typeSpecBuilder, CodeGenSpec codeGenSpec) {
        for (CodeGenSpec.FieldSpec fieldSpec : codeGenSpec.getFields()) {
            if (isCollectionOrMap(fieldSpec.getType().nativeType)) {
                addInnerClassesForCollectionHandlersImpl(typeSpecBuilder, fieldSpec.getType());
            }
        }
    }

    private void addInnerClassesForCollectionHandlersImpl(TypeSpec.Builder typeSpecBuilder, CodeGenSpec.Type fieldSpec) {
        // pass the field name and use fieldName + Handler + (counter++) to have names of the handlers
        /*typeSpecBuilder.addType(
        TypeSpec.classBuilder(fieldSpec. + "Handler")
                .addMethod(getSerializer(thePojo, codeGenSpec))
                .addMethod(getDeserializer(thePojo, codeGenSpec))
                .superclass(ParameterizedTypeName.get(ClassName.get(PibifyGenerated.class), thePojo))
        );*/
    }

    private MethodSpec getSerializer(ClassName thePojo, CodeGenSpec codeGenSpec) throws CodeGenException {

        try {
            MethodSpec.Builder builder = MethodSpec.methodBuilder("serialize")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .returns(byte[].class)
                    .addException(PibifyCodeExecException.class)
                    .addParameter(thePojo, "object")
                    .addStatement("$T serializer = new $T()", ISerializer.class, PibifySerializer.class)
                    .beginControlFlow("try");

            for (CodeGenSpec.FieldSpec fieldSpec : codeGenSpec.getFields()) {
                switch (fieldSpec.getType().nativeType) {
                    case ARRAY:
                    case COLLECTION:
                        generateSerializerForArrayOrCollection(fieldSpec, builder);
                        break;
                    case MAP:
                        generateSerializerForMap(fieldSpec, builder);
                        break;
                    case OBJECT:
                        generateSerializerForObjectReference(codeGenSpec, fieldSpec, builder);
                        break;
                    default:
                        builder.addStatement("serializer.write" + fieldSpec.getType().nativeType.getReadWriteMethodName()
                                + "(" + fieldSpec.getIndex() + ", object." + fieldSpec.getGetter() + "())");
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

    private void generateSerializerForArrayOrCollection(CodeGenSpec.FieldSpec fieldSpec, MethodSpec.Builder builder) {
        if (!(fieldSpec.getType().nativeType == CodeGenSpec.DataType.ARRAY ||
                fieldSpec.getType().nativeType == CodeGenSpec.DataType.COLLECTION)) {
            throw new IllegalArgumentException("generateSerializerForArrayOrCollection invoked for non-array types " + fieldSpec.getName());
        }

        CodeGenSpec.Type realizedType = fieldSpec.getType().containerTypes.get(0);
        if (realizedType.nativeType == CodeGenSpec.DataType.UNKNOWN) {
            // TODO  unknowns to be handled as a byte[] via object mapper.
            //
            throw new UnsupportedOperationException("Unknown arrays/collections not supported right now");
        } else {

            if (realizedType.nativeType == CodeGenSpec.DataType.OBJECT) {
                CodeGenSpec refSpec = realizedType.referenceType;
                builder.addStatement("$T $LHandler = new $T()",
                        ParameterizedTypeName.get(ClassName.get(PibifyGenerated.class),
                                ClassName.get(refSpec.getPackageName(), refSpec.getClassName())
                        ), fieldSpec.getName(),
                        ClassName.get("com.flipkart.pibify.generated." + refSpec.getPackageName(),
                                refSpec.getClassName() + "Handler"));
            }

            builder.beginControlFlow("for ($T val : object.$L())",
                    getReferenceTypeForContainers(realizedType, false), fieldSpec.getGetter());

            if (realizedType.nativeType == CodeGenSpec.DataType.OBJECT) {
                builder.addStatement("serializer.writeObjectAsBytes($L, $LHandler.serialize(val))",
                        fieldSpec.getIndex(), fieldSpec.getName());
            } else {
                builder.addStatement("serializer.write$L($L, val)",
                        realizedType.nativeType.getReadWriteMethodName(), fieldSpec.getIndex());
            }

            builder.endControlFlow();
        }
    }

    // Can return a ClassName of a Class (for java natives)
    // @returns Either the javapoet class name, or java reflection class

    private Object getReferenceTypeForContainers(CodeGenSpec.Type realizedType, boolean preferAutoboxed) {
        if (realizedType.nativeType == CodeGenSpec.DataType.OBJECT) {
            return ClassName.get(realizedType.referenceType.getPackageName(), realizedType.referenceType.getClassName());
        } else {

            // if native type is collection or map
            // get deep name, else return native types clazz
            if (realizedType.nativeType == CodeGenSpec.DataType.COLLECTION) {
                return Collection.class;
            } else if (realizedType.nativeType == CodeGenSpec.DataType.MAP) {
                return Map.class;
            } else {
                if (preferAutoboxed) {
                    return ClassName.get(realizedType.nativeType.getAutoboxedClass());
                } else {
                    return realizedType.nativeType.getClazz();
                }
            }
        }
    }

    private void generateSerializerForMap(CodeGenSpec.FieldSpec fieldSpec, MethodSpec.Builder builder) {

        if (fieldSpec.getType().nativeType != CodeGenSpec.DataType.MAP) {
            throw new IllegalArgumentException("generateSerializerForMap invoked for non-map types " + fieldSpec.getName());
        }

        CodeGenSpec.Type keyType = fieldSpec.getType().containerTypes.get(0);
        CodeGenSpec.Type valueType = fieldSpec.getType().containerTypes.get(1);
        if (keyType.nativeType == CodeGenSpec.DataType.UNKNOWN
                || valueType.nativeType == CodeGenSpec.DataType.UNKNOWN) {
            throw new UnsupportedOperationException("Unknown k/v in maps not supported right now");
        } else {

            if (keyType.nativeType == CodeGenSpec.DataType.OBJECT) {
                CodeGenSpec refSpec = keyType.referenceType;
                builder.addStatement("$T key$LHandler = new $T()",
                        ParameterizedTypeName.get(ClassName.get(PibifyGenerated.class),
                                ClassName.get(refSpec.getPackageName(), refSpec.getClassName())
                        ), fieldSpec.getName(),
                        ClassName.get("com.flipkart.pibify.generated." + refSpec.getPackageName(),
                                refSpec.getClassName() + "Handler"));
            }

            if (valueType.nativeType == CodeGenSpec.DataType.OBJECT) {
                CodeGenSpec refSpec = valueType.referenceType;
                builder.addStatement("$T value$LHandler = new $T()",
                        ParameterizedTypeName.get(ClassName.get(PibifyGenerated.class),
                                ClassName.get(refSpec.getPackageName(), refSpec.getClassName())
                        ), fieldSpec.getName(),
                        ClassName.get("com.flipkart.pibify.generated." + refSpec.getPackageName(),
                                refSpec.getClassName() + "Handler"));
            }

            builder.beginControlFlow("for ($T<$L, $L> entry : object.$L().entrySet())",
                            Map.Entry.class,
                            getReferenceTypeForContainers(keyType, true),
                            getReferenceTypeForContainers(valueType, true),
                            fieldSpec.getGetter())
                    .addStatement("$T $LSerializer = new $T()", ISerializer.class, fieldSpec.getName(),
                            PibifySerializer.class);

            if (keyType.nativeType == CodeGenSpec.DataType.OBJECT) {
                builder.addStatement("$LSerializer.writeObjectAsBytes(1, key$LHandler.serialize(entry.getKey()))", fieldSpec.getName(), fieldSpec.getName());
            } else {
                builder.addStatement("$LSerializer.write$L(1, entry.getKey())", fieldSpec.getName(),
                        keyType.nativeType.getReadWriteMethodName());
            }

            if (valueType.nativeType == CodeGenSpec.DataType.OBJECT) {
                builder.addStatement("$LSerializer.writeObjectAsBytes(2, value$LHandler.serialize(entry.getValue()))", fieldSpec.getName(), fieldSpec.getName());
            } else {
                builder.addStatement("$LSerializer.write$L(2, entry.getValue())", fieldSpec.getName(),
                        valueType.nativeType.getReadWriteMethodName());
            }

            builder.addStatement("serializer.writeObjectAsBytes($L, $LSerializer.serialize())", fieldSpec.getIndex(),
                            fieldSpec.getName())
                    .endControlFlow();
        }
    }

    private void generateSerializerForObjectReference(CodeGenSpec codeGenSpec, CodeGenSpec.FieldSpec fieldSpec, MethodSpec.Builder builder) {
        if (fieldSpec.getType().nativeType != CodeGenSpec.DataType.OBJECT) {
            throw new IllegalArgumentException("generateSerializerForObjectReference invoked for non-reference types "
                    + fieldSpec.getName());
        }

        CodeGenSpec refSpec = fieldSpec.getType().referenceType;

        builder.addStatement("$T " + fieldSpec.getName() + "Handler = new $T()",
                ParameterizedTypeName.get(ClassName.get(PibifyGenerated.class),
                        ClassName.get(refSpec.getPackageName(), refSpec.getClassName())
                ),
                ClassName.get("com.flipkart.pibify.generated." + refSpec.getPackageName(),
                        refSpec.getClassName() + "Handler"));
        builder.addStatement("serializer.writeObjectAsBytes($L, referenceHandler.serialize(object." + fieldSpec.getGetter() + "()))",
                fieldSpec.getIndex());
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
        switch (fieldSpec.getType().nativeType) {
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
                builder.addStatement("case " +
                        TagPredictor.getTagBasedOnField(fieldSpec.getIndex(), getClassForTag(fieldSpec)) + ": \n $>" +
                        "object." + fieldSpec.getSetter()
                        + "(" + getCastIfRequired(fieldSpec.getType().nativeType)
                        + "deserializer.read" + fieldSpec.getType().nativeType.getReadWriteMethodName() + "())$<");
                break;
        }
    }

    private void addMapDeserializer(CodeGenSpec.FieldSpec fieldSpec, MethodSpec.Builder builder) throws InvalidPibifyAnnotation {
        CodeGenSpec.Type keyType = fieldSpec.getType().containerTypes.get(0);
        CodeGenSpec.Type valueType = fieldSpec.getType().containerTypes.get(1);

        int tag = TagPredictor.getTagBasedOnField(fieldSpec.getIndex(), byte[].class);

        builder.addStatement("case " +
                        tag + ": \n$>" +
                        "byte[] bytes$L = deserializer.readObjectAsBytes()", tag)
                .beginControlFlow("if (object.$L() == null)", fieldSpec.getGetter())
                .addStatement("object.$L(new $T())", fieldSpec.getSetter(), HashMap.class)
                .endControlFlow()
                .addStatement("$T deserializer$L = new $T(bytes$L)", IDeserializer.class, tag, PibifyDeserializer.class, tag)
                .addStatement("int tag$L = deserializer$L.getNextTag()", tag, tag);

        if (keyType.nativeType == CodeGenSpec.DataType.OBJECT) {
            CodeGenSpec refSpec = keyType.referenceType;
            builder.addStatement("$T key$LHandler = new $T()",
                    ParameterizedTypeName.get(ClassName.get(PibifyGenerated.class),
                            ClassName.get(refSpec.getPackageName(), refSpec.getClassName())
                    ), fieldSpec.getName(),
                    ClassName.get("com.flipkart.pibify.generated." + refSpec.getPackageName(),
                            refSpec.getClassName() + "Handler"));

            builder.addStatement("$T key = key$LHandler.deserialize(deserializer$L.read$L())",
                    getReferenceTypeForContainers(keyType, true), fieldSpec.getName(), tag,
                    keyType.nativeType.getReadWriteMethodName());
        } else {
            builder.addStatement("$T key = deserializer$L.read$L()", getReferenceTypeForContainers(keyType, true), tag, keyType.nativeType.getReadWriteMethodName());
        }

        builder.addStatement("tag$L = deserializer$L.getNextTag()", tag, tag);

        if (valueType.nativeType == CodeGenSpec.DataType.OBJECT) {

            CodeGenSpec refSpec = valueType.referenceType;
            builder.addStatement("$T value$LHandler = new $T()",
                    ParameterizedTypeName.get(ClassName.get(PibifyGenerated.class),
                            ClassName.get(refSpec.getPackageName(), refSpec.getClassName())
                    ), fieldSpec.getName(),
                    ClassName.get("com.flipkart.pibify.generated." + refSpec.getPackageName(),
                            refSpec.getClassName() + "Handler"));

            builder.addStatement("$T value = value$LHandler.deserialize(deserializer$L.read$L())",
                    getReferenceTypeForContainers(valueType, true), fieldSpec.getName(), tag,
                    valueType.nativeType.getReadWriteMethodName());
        } else {
            builder.addStatement("$T value = deserializer$L.read$L()", getReferenceTypeForContainers(valueType, true), tag, valueType.nativeType.getReadWriteMethodName());
        }

        builder.addStatement("object.$L().put(key, value)", fieldSpec.getGetter());

    }

    private void addCollectionDeserializer(CodeGenSpec.FieldSpec fieldSpec, MethodSpec.Builder builder) throws InvalidPibifyAnnotation {

        CodeGenSpec.Type realizedType = fieldSpec.getType().containerTypes.get(0);

        int tag = TagPredictor.getTagBasedOnField(fieldSpec.getIndex(), getClassForTag(fieldSpec));

        if (realizedType.nativeType == CodeGenSpec.DataType.OBJECT) {

            CodeGenSpec refSpec = realizedType.referenceType;
            builder.addStatement("case $L: \n$T $LHandler = new $T()", tag,
                    ParameterizedTypeName.get(ClassName.get(PibifyGenerated.class),
                            ClassName.get(refSpec.getPackageName(), refSpec.getClassName())
                    ), fieldSpec.getName(),
                    ClassName.get("com.flipkart.pibify.generated." + refSpec.getPackageName(),
                            refSpec.getClassName() + "Handler"));

            builder.addStatement("$>$T val$L = $LHandler.deserialize(deserializer.read$L())",
                    getReferenceTypeForContainers(fieldSpec.getType().containerTypes.get(0), false), tag,
                    fieldSpec.getName(), realizedType.nativeType.getReadWriteMethodName());
        } else {
            builder.addStatement("case $L:\n$T val$L = deserializer.read$L()", tag,
                    getReferenceTypeForContainers(fieldSpec.getType().containerTypes.get(0), false), tag,
                    realizedType.nativeType.getReadWriteMethodName());
        }


        builder.beginControlFlow("$>if (object.$L() == null)$<", fieldSpec.getGetter())
                .addStatement("$>object.$L($L)$<", fieldSpec.getSetter(), getCollectionCreator(fieldSpec))
                .endControlFlow()
                .addStatement("$>object.$L().add(val$L)$<", fieldSpec.getGetter(), tag);

    }

    private String getCollectionCreator(CodeGenSpec.FieldSpec fieldSpec) {
        switch (fieldSpec.getType().collectionType) {
            case LIST:
                return "new java.util.ArrayList()";
            case SET:
                return "new java.util.HashSet()";
            case QUEUE:
                return "new java.util.PriorityQueue()";
            case DEQUE:
                return "new java.util.Stack()";
            default:
                throw new UnsupportedOperationException("Collection type " +
                        fieldSpec.getType().collectionType + " not supported");
        }

    }

    private void addArrayDeserializer(CodeGenSpec.FieldSpec fieldSpec, MethodSpec.Builder builder) throws InvalidPibifyAnnotation {
        CodeGenSpec.Type realizedType = fieldSpec.getType().containerTypes.get(0);

        if (realizedType.nativeType == CodeGenSpec.DataType.UNKNOWN) return;

        int tag = TagPredictor.getTagBasedOnField(fieldSpec.getIndex(), getClassForTag(fieldSpec));
        Object typeForContainers = getReferenceTypeForContainers(fieldSpec.getType().containerTypes.get(0), false);
        builder.addStatement("case " +
                        tag + ": \n$>" +
                        "$T[] newArray$L", typeForContainers, tag)
                .addStatement("$>$T[] oldArray$L = object." + fieldSpec.getGetter() + "()$<", typeForContainers, tag);
        if (realizedType.nativeType == CodeGenSpec.DataType.OBJECT) {
            CodeGenSpec refSpec = realizedType.referenceType;
            builder.addStatement("$T $LHandler = new $T()",
                    ParameterizedTypeName.get(ClassName.get(PibifyGenerated.class),
                            ClassName.get(refSpec.getPackageName(), refSpec.getClassName())
                    ), fieldSpec.getName(),
                    ClassName.get("com.flipkart.pibify.generated." + refSpec.getPackageName(),
                            refSpec.getClassName() + "Handler"));

            builder.addStatement("$>$T val$L = $LHandler.deserialize(deserializer.read$L())",
                    getReferenceTypeForContainers(fieldSpec.getType().containerTypes.get(0), false), tag,
                    fieldSpec.getName(), realizedType.nativeType.getReadWriteMethodName());
        } else {
            builder.addStatement("$>$T val$L = deserializer.read$L()$<", typeForContainers, tag, realizedType.nativeType.getReadWriteMethodName());
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
        CodeGenSpec refSpec = fieldSpec.getType().referenceType;
        int tag = TagPredictor.getTagBasedOnField(fieldSpec.getIndex(), byte[].class);

        builder.addStatement("case " +
                        tag + ": \n$>" +
                        "$T " + fieldSpec.getName() + "Handler$L = new $T()",
                ParameterizedTypeName.get(ClassName.get(PibifyGenerated.class),
                        ClassName.get(refSpec.getPackageName(), refSpec.getClassName())
                ),
                tag,
                ClassName.get("com.flipkart.pibify.generated." + refSpec.getPackageName(),
                        refSpec.getClassName() + "Handler"));

        builder.addStatement("$>object." + fieldSpec.getSetter() + "(" + fieldSpec.getName()
                + "Handler$L.deserialize(deserializer.readObjectAsBytes()))$<", tag);

    }

    private Class<?> getClassForTag(CodeGenSpec.FieldSpec fieldSpec) {
        switch (fieldSpec.getType().nativeType) {
            case ARRAY:
            case COLLECTION:
                CodeGenSpec.DataType containerNativeType = fieldSpec.getType().containerTypes.get(0).nativeType;
                if (containerNativeType == CodeGenSpec.DataType.OBJECT
                        || containerNativeType == CodeGenSpec.DataType.COLLECTION
                        || containerNativeType == CodeGenSpec.DataType.MAP) {
                    return Object.class;
                } else {
                    return fieldSpec.getType().containerTypes.get(0).nativeType.getClazz();
                }

            case OBJECT:
                return Object.class;
            default:
                return fieldSpec.getType().nativeType.getClazz();
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
