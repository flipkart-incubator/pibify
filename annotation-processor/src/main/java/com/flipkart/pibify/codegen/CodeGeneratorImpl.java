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

/**
 * This class is used for generating the JavaFile given a CodeGenSpec
 * Author bageshwar.pn
 * Date 10/08/24
 */
public class CodeGeneratorImpl implements ICodeGenerator {

    @Override
    public JavaFile generate(CodeGenSpec codeGenSpec) throws IOException, CodeGenException {

        ClassName thePojo = ClassName.get(codeGenSpec.getPackageName(), codeGenSpec.getClassName());

        TypeSpec pibifyGeneratedHandler = TypeSpec.classBuilder(codeGenSpec.getClassName() + "Handler")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(getSerializer(thePojo, codeGenSpec))
                .addMethod(getDeserializer(thePojo, codeGenSpec))
                .superclass(ParameterizedTypeName.get(ClassName.get(PibifyGenerated.class), thePojo))
                .build();

        return JavaFile.builder("com.flipkart.pibify.generated."
                        + codeGenSpec.getPackageName(), pibifyGeneratedHandler)
                .build();
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
                        generateSerializerForArray(fieldSpec, builder);
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

    private void generateSerializerForArray(CodeGenSpec.FieldSpec fieldSpec, MethodSpec.Builder builder) {
        if (fieldSpec.getType().nativeType != CodeGenSpec.DataType.ARRAY) {
            throw new IllegalArgumentException("generateSerializerForArray invoked for non-array types");
        }

        /*
        serializer.writeShort(9, pojo.getaShort());
        * * for (int i = 0; i < additionalItems_.size(); i++) {
         *       output.writeMessage(1, additionalItems_.get(i));
         *     }
        * */
        builder.beginControlFlow("for (int i = 0; i < object." + fieldSpec.getGetter() + "().length; i++)");
        builder.addStatement("serializer.write" + fieldSpec.getType().containerTypes.get(0).nativeType.getReadWriteMethodName()
                + "(" + fieldSpec.getIndex() + ", object." + fieldSpec.getGetter() + "()[i])");
        builder.endControlFlow();

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
        CodeGenSpec.DataType realizedType;
        switch (fieldSpec.getType().nativeType) {
            case ARRAY:
                realizedType = fieldSpec.getType().containerTypes.get(0).nativeType;
                int tag = TagPredictor.getTagBasedOnField(fieldSpec.getIndex(), getClassForTag(fieldSpec));
                builder.addStatement("case " +
                                tag + ": \n$>" +
                                "$T[] newArray$L", realizedType.getClazz(), tag)
                        .addStatement("$>$T[] oldArray$L = object." + fieldSpec.getGetter() + "()$<", realizedType.getClazz(), tag)
                        .addStatement("$>$T val$L = deserializer.read" + realizedType.getReadWriteMethodName() + "()$<", realizedType.getClazz(), tag)
                        .beginControlFlow("$>if (oldArray$L == null)$<", tag)
                        .addStatement("$>newArray$L = new $T[]{val$L}$<", tag, realizedType.getClazz(), tag)
                        .endControlFlow()
                        .beginControlFlow("$>else$<")
                        .addStatement("$>newArray$L = $T.copyOf(oldArray$L, oldArray$L.length + 1)$<", tag, Arrays.class, tag, tag)
                        .addStatement("$>newArray$L[oldArray$L.length] = val$L$<", tag, tag, tag)
                        .endControlFlow()
                        .addStatement("$>object." + fieldSpec.getSetter()
                                + "(newArray$L)$<", tag)

                ;

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

    private Class<?> getClassForTag(CodeGenSpec.FieldSpec fieldSpec) {
        switch (fieldSpec.getType().nativeType) {
            case ARRAY:
            case COLLECTION:
                return fieldSpec.getType().containerTypes.get(0).nativeType.getClazz();
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
