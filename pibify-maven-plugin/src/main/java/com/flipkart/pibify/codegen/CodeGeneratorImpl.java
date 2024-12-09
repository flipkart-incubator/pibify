package com.flipkart.pibify.codegen;

import com.flipkart.pibify.codegen.stub.PibifyGenerated;
import com.flipkart.pibify.codegen.stub.PibifyObjectHandler;
import com.flipkart.pibify.codegen.stub.SerializationContext;
import com.flipkart.pibify.core.PibifyConfiguration;
import com.flipkart.pibify.serde.IDeserializer;
import com.flipkart.pibify.serde.ISerializer;
import com.flipkart.pibify.validation.InvalidPibifyAnnotation;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.flipkart.pibify.codegen.CodeGenUtil.isArray;
import static com.flipkart.pibify.codegen.CodeGenUtil.isCollection;
import static com.flipkart.pibify.codegen.CodeGenUtil.isCollectionOrMap;
import static com.flipkart.pibify.codegen.CodeGenUtil.isJavaLangObject;
import static com.flipkart.pibify.codegen.CodeGenUtil.isNotNative;
import static com.flipkart.pibify.core.Constants.PIBIFY_GENERATED_PACKAGE_NAME;

/**
 * This class is used for generating the JavaFile given a CodeGenSpec
 * Author bageshwar.pn
 * Date 10/08/24
 */
public class CodeGeneratorImpl implements ICodeGenerator {

    private final String handlerCacheClassName;

    /*
    Internal State
     */
    private TypeSpec.Builder classBuilder;
    private Map<TypeName, String> fields;

    public CodeGeneratorImpl(String handlerCacheClassName) {
        this.handlerCacheClassName = handlerCacheClassName + ".getInstance().getHandler";
        fields = new HashMap<>();
    }

    private static void validate(CodeGenSpec codeGenSpec) throws CodeGenException {
        if (codeGenSpec.getFields().isEmpty()) {
            throw new CodeGenException(codeGenSpec.getJpClassName() + " does not contain any pibify fields");
        }

        /*
         * Not generating handlers for abstract classes because we cannot create instances of those classes.
         * The subclasses of such abstract classes will take care of super class fields
         */
        if (codeGenSpec.isAbstract()) {
            throw new CodeGenException("Cannot generate handlers for abstract class: " + codeGenSpec.getJpClassName());
        }
    }

    /**
     * This method is not thread-safe
     *
     * @param codeGenSpec
     * @return
     * @throws IOException
     * @throws CodeGenException
     */
    @Override
    public synchronized JavaFileWrapper generate(CodeGenSpec codeGenSpec) throws IOException, CodeGenException {
        clearState();
        validate(codeGenSpec);

        TypeSpec.Builder typeSpecBuilder = getTypeSpecBuilder(codeGenSpec);
        typeSpecBuilder.addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        addInnerClasses(typeSpecBuilder, codeGenSpec);

        TypeSpec pibifyGeneratedHandler = typeSpecBuilder.build();

        String packageName = PIBIFY_GENERATED_PACKAGE_NAME + codeGenSpec.getPackageName();
        JavaFileWrapper wrapper = new JavaFileWrapper();
        wrapper.setPackageName(packageName);
        // TODO move this class name computation to a common place
        wrapper.setClassName(ClassName.get(packageName, codeGenSpec.getClassName() + "Handler"));
        wrapper.setJavaFile(JavaFile.builder(packageName, pibifyGeneratedHandler)
                .build());
        return wrapper;
    }

    private TypeSpec.Builder getTypeSpecBuilder(CodeGenSpec codeGenSpec) throws CodeGenException {
        ClassName thePojo = codeGenSpec.getJpClassName();
        TypeSpec.Builder previousClassBuilder = this.classBuilder;
        Map<TypeName, String> previousFields = this.fields;
        this.classBuilder = TypeSpec.classBuilder(codeGenSpec.getClassName() + "Handler");
        this.fields = new HashMap<>();
        TypeSpec.Builder builder = classBuilder.addMethod(getSerializer(thePojo, codeGenSpec))
                .addMethod(getDeserializer(thePojo, codeGenSpec))
                .superclass(ParameterizedTypeName.get(ClassName.get(PibifyGenerated.class), thePojo))
                .addMethod(getInitializeMethod(true));

        this.classBuilder = previousClassBuilder;
        this.fields = previousFields;
        return builder;
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

    private void addInnerClasses(TypeSpec.Builder typeSpecBuilder, CodeGenSpec codeGenSpec) throws CodeGenException {
        AtomicInteger counter = new AtomicInteger(1);
        Map<String, String> mapOfGenericSignatureToHandlerName = new HashMap<>();

        Set<CodeGenSpec> innerClassReferences = new HashSet<>();

        for (CodeGenSpec.FieldSpec fieldSpec : codeGenSpec.getFields()) {
            if (isCollectionOrMap(fieldSpec.getType().getNativeType())) {
                addInnerClassesForCollectionHandlersImpl(typeSpecBuilder, fieldSpec.getType(), counter, mapOfGenericSignatureToHandlerName);
            } else if (CodeGenUtil.isReferenceOfInnerClass(fieldSpec)) {
                innerClassReferences.add(fieldSpec.getType().getReferenceType());
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

        for (CodeGenSpec reference : innerClassReferences) {
            TypeSpec.Builder specBuilderForInnerClass = getTypeSpecBuilder(reference);
            // Need inner classes to be static to enable static block for the reference map
            specBuilderForInnerClass.addModifiers(Modifier.STATIC);
            addInnerClasses(specBuilderForInnerClass, reference);
            typeSpecBuilder.addType(specBuilderForInnerClass.build());
        }
    }

    private void addInnerClassesForCollectionHandlersImpl(TypeSpec.Builder typeSpecBuilder, CodeGenSpec.Type fieldSpec,
                                                          AtomicInteger counter, Map<String, String> mapOfGenericSignatureToHandlerName) throws CodeGenException {
        // pass the field name and use fieldName + Handler + (counter++) to have names of the handlers
        String className = "InternalHandler" + counter.incrementAndGet();
        TypeSpec.Builder innerClassBuilder = TypeSpec.classBuilder(className);

        TypeSpec.Builder previousClassBuilder = this.classBuilder;
        Map<TypeName, String> previousFields = this.fields;
        this.classBuilder = innerClassBuilder;
        this.fields = new HashMap<>();

        typeSpecBuilder.addType(
                innerClassBuilder.addModifiers(Modifier.STATIC)
                        .addMethod(getSerializerForCollectionHandler(fieldSpec))
                        .addMethod(getDeserializerForCollectionHandler(fieldSpec))
                        .addMethod(getInitializeMethod(false))
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

        this.classBuilder = previousClassBuilder;
        this.fields = previousFields;
    }

    private MethodSpec getInitializeMethod(boolean initializeInternals) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("initialize")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class);

        for (Map.Entry<TypeName, String> entry : fields.entrySet()) {
            builder.addStatement("$L = $L($T.class).get()", entry.getValue(), handlerCacheClassName, entry.getKey());
        }

        if (initializeInternals) {
            builder.beginControlFlow("for (PibifyGenerated internalHandler : HANDLER_MAP.values())")
                    .addStatement("internalHandler.initialize()")
                    .endControlFlow();
        }


        return builder.build();

    }

    private void addHandlerForObjectReference(CodeGenSpec.FieldSpec fieldSpec, MethodSpec.Builder builder, CodeGenSpec codeGenSpec) {
        addHandlerForObjectReference(fieldSpec.getName(), builder, codeGenSpec);
    }

    private static void addWriterBlockForCollectionHandler(CodeGenSpec.Type fieldSpec, MethodSpec.Builder builder, int index, String value, int writeIndex, String handler) {
        if (CodeGenUtil.isNotNative(fieldSpec.getContainerTypes().get(index).getNativeType())) {
            builder.addStatement("serializer.writeObject($L, $L, $L, context)", writeIndex, handler, value);
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
                    .addException(PibifyCodeExecException.class)
                    .addParameter(fieldSpec.getjPTypeName(), "object")
                    .addParameter(ISerializer.class, "serializer")
                    .addParameter(SerializationContext.class, "context")
                    .beginControlFlow("if (object == null)")
                    .addStatement("return")
                    .endControlFlow();

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

            builder.nextControlFlow("catch ($T e)", Exception.class)
                    .addStatement("throw new $T(e)", PibifyCodeExecException.class)
                    .endControlFlow();

            return builder.build();
        } catch (Exception e) {
            throw new CodeGenException(e.getMessage(), e);
        }
    }

    private static boolean isString(CodeGenSpec.FieldSpec fieldSpec) {
        return fieldSpec.getType().getjPTypeName().equals(TypeName.get(String.class));
    }

    private MethodSpec getSerializer(ClassName thePojo, CodeGenSpec codeGenSpec) throws CodeGenException {

        try {
            MethodSpec.Builder builder = MethodSpec.methodBuilder("serialize")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .addException(PibifyCodeExecException.class)
                    .addParameter(thePojo, "object")
                    .addParameter(ISerializer.class, "serializer")
                    .addParameter(SerializationContext.class, "context")
                    .beginControlFlow("if (object == null)")
                    .addStatement("return")
                    .endControlFlow()
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
                        builder.addStatement("serializer.writeEnum($L, object.$L$L)", fieldSpec.getIndex(), fieldSpec.getGetter(), handleGetterBean(fieldSpec));
                        break;
                    default:
                        addDefaultSerializationBlock(fieldSpec, builder);
                }
            }

            builder.nextControlFlow("catch ($T e)", Exception.class)
                    .addStatement("throw new $T(e)", PibifyCodeExecException.class)
                    .endControlFlow();

            return builder.build();
        } catch (Exception e) {
            throw new CodeGenException(e.getMessage(), e);
        }
    }

    // This method takes care of public fields vs methods
    private String handleGetterBean(CodeGenSpec.FieldSpec fieldSpec) {
        return fieldSpec.hasBeanMethods() ? "()" : "";
    }

    private static ClassName getAbstractOrConcreteJPClassName(CodeGenSpec codeGenSpec) {
        return (codeGenSpec == null || codeGenSpec.isAbstract())
                ? ClassName.OBJECT
                : codeGenSpec.getJpClassName();
    }

    private static TypeName getAbstractOrConcreteJPTypeName(CodeGenSpec.Type containerType) {
        return (containerType.getReferenceType() != null && containerType.getReferenceType().isAbstract())
                ? TypeName.OBJECT
                : containerType.getjPTypeName();
    }

    private void addHandlerForObjectReference(String name, MethodSpec.Builder builder, CodeGenSpec codeGenSpec) {
        ClassName reference;

        if (CodeGenUtil.isJavaLangObject(codeGenSpec)) {
            // Special handling for object references
            reference = ClassName.get(PibifyObjectHandler.class);
        } else {
            String handlerClassName = codeGenSpec.getClassName();
            String packageName = "";
            if (!codeGenSpec.isInnerClass()) {
                packageName = PIBIFY_GENERATED_PACKAGE_NAME + codeGenSpec.getPackageName();
            }
            reference = ClassName.get(packageName, handlerClassName);
        }

        String variableName = name + "Handler";
        ClassName referenceTypeClassName = getAbstractOrConcreteJPClassName(codeGenSpec);
        ParameterizedTypeName type = ParameterizedTypeName.get(ClassName.get(PibifyGenerated.class), referenceTypeClassName);
        // At a new field for this handler.
        if (!fields.containsKey(referenceTypeClassName)) {
            FieldSpec.Builder fieldBuilder = FieldSpec.builder(type, variableName, Modifier.PRIVATE);
            classBuilder.addField(fieldBuilder.build());
            fields.put(referenceTypeClassName, variableName);
        }

        // If the variable name in context is not the same as the field added, assign it.
        if (!variableName.equals(fields.get(referenceTypeClassName))) {
            builder.addStatement("$T $L = $L", type, variableName, fields.get(referenceTypeClassName));
        }
    }

    private MethodSpec getDeserializerForCollectionHandler(CodeGenSpec.Type fieldSpec) throws CodeGenException {
        try {
            MethodSpec.Builder builder = MethodSpec.methodBuilder("deserialize")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .returns(fieldSpec.getjPTypeName())
                    .addException(PibifyCodeExecException.class)
                    .addParameter(IDeserializer.class, "deserializer")
                    .addParameter(ParameterizedTypeName.get(ClassName.get(Class.class), fieldSpec.getjPTypeName()), "clazz")
                    .addParameter(SerializationContext.class, "context")
                    .beginControlFlow("try")
                    .addStatement("int tag = deserializer.getNextTag()");

            if (fieldSpec.getNativeType() == CodeGenSpec.DataType.COLLECTION) {
                builder.addStatement("$T object = new $T()", fieldSpec.getjPTypeName(), fieldSpec.getNewInstanceType());
            } else if (fieldSpec.getNativeType() == CodeGenSpec.DataType.MAP) {
                // TBF
                //builder.addStatement("$T object = new $T()", fieldSpec.getjPTypeName(), HashMap.class);
                builder.addStatement("$T object = new $T()", fieldSpec.getjPTypeName(), fieldSpec.getNewInstanceType());
            } else {
                throw new UnsupportedOperationException();
            }

            addHandlerBasedOnDatatype(fieldSpec, builder);

            if (fieldSpec.getNativeType() == CodeGenSpec.DataType.COLLECTION) {
                CodeGenSpec.Type containerType = fieldSpec.getContainerTypes().get(0);
                TypeName jpTypeName = getAbstractOrConcreteJPTypeName(containerType);
                builder.addStatement("$T value", jpTypeName);
                builder.beginControlFlow("while (tag != 0 && tag != PibifyGenerated.getEndObjectTag()) ");

                if (isNotNative(containerType.getNativeType())) {
                    //value = refHandler.deserialize(deserializer, Class.class);
                    builder.addStatement("value = valueHandler.deserialize(deserializer, $L, context)",
                            getClassTypeForObjectMapperHandler(containerType, getAbstractOrConcreteJPClassName(containerType.getReferenceType())));
                    //getClassTypeForObjectMapperHandler(containerType, jpTypeName));

                    // If we are processing an Object reference, get the MapEntry and use the value
                    if (isJavaLangObject(jpTypeName)) {
                        builder.addStatement("value = ((Map.Entry<String,Object>)(value)).getValue()");
                    }
                } else {
                    //value = deserializer.readString();
                    if (containerType.getNativeType() == CodeGenSpec.DataType.ENUM) {
                        builder.addStatement("value = $T.values()[deserializer.readEnum()]", jpTypeName);
                    } else {
                        builder.addStatement("value = deserializer.read$L()", containerType.getNativeType().getReadWriteMethodName());
                    }
                }

                //collection.add(value);
                // refType will be null for native types
                if (containerType.getReferenceType() != null && containerType.getReferenceType().isAbstract()) {
                    // need a typecast from object
                    builder.addStatement("object.add(($T)value)", containerType.getReferenceType().getJpClassName());
                } else {
                    builder.addStatement("object.add(value)");
                }

            } else if (fieldSpec.getNativeType() == CodeGenSpec.DataType.MAP) {
                // TODO The key and value blocks are identical and can be simplified
                CodeGenSpec.Type keyContainerType = fieldSpec.getContainerTypes().get(0);
                CodeGenSpec.Type valueContainerType = fieldSpec.getContainerTypes().get(1);
                TypeName keyJpTypeName = getAbstractOrConcreteJPTypeName(keyContainerType);
                TypeName valueJpTypeName = getAbstractOrConcreteJPTypeName(valueContainerType);

                builder.addStatement("$T key", keyJpTypeName);
                builder.addStatement("$T value", valueJpTypeName);
                builder.beginControlFlow("while (tag != 0 && tag != PibifyGenerated.getEndObjectTag())");

                if (isNotNative(keyContainerType.getNativeType())) {
                    builder.addStatement("key = keyHandler.deserialize(deserializer, $L, context)",
                            getClassTypeForObjectMapperHandler(keyContainerType, getAbstractOrConcreteJPClassName(keyContainerType.getReferenceType())));
                    // If we are processing an Object reference, get the MapEntry and use the value
                    if (isJavaLangObject(keyJpTypeName)) {
                        builder.addStatement("key = ((Map.Entry<String,Object>)(key)).getValue()");
                    }
                } else {
                    if (keyContainerType.getNativeType() == CodeGenSpec.DataType.ENUM) {
                        builder.addStatement("key = $T.values()[deserializer.readEnum()]",
                                keyContainerType.getjPTypeName());
                    } else {
                        builder.addStatement("key = deserializer.read$L()",
                                keyContainerType.getNativeType().getReadWriteMethodName());
                    }
                }
                builder.addStatement("tag = deserializer.getNextTag()");

                if (isNotNative(valueContainerType.getNativeType())) {
                    builder.addStatement("value = valueHandler.deserialize(deserializer, $L, context)",
                            getClassTypeForObjectMapperHandler(valueContainerType, getAbstractOrConcreteJPClassName(valueContainerType.getReferenceType())));
                    // If we are processing an Object reference, get the MapEntry and use the value
                    if (isJavaLangObject(valueJpTypeName)) {
                        builder.addStatement("value = ((Map.Entry<String,Object>)(value)).getValue()");
                    }
                } else {
                    if (valueContainerType.getNativeType() == CodeGenSpec.DataType.ENUM) {
                        builder.addStatement("value = $T.values()[deserializer.readEnum()]",
                                valueContainerType.getjPTypeName());
                    } else {
                        builder.addStatement("value = deserializer.read$L()",
                                valueContainerType.getNativeType().getReadWriteMethodName());
                    }
                }

                String keyTypecast = "";
                String valueTypeCast = "";
                if (keyContainerType.getReferenceType() != null && keyContainerType.getReferenceType().isAbstract()) {
                    keyTypecast = "(" + keyContainerType.getjPTypeName() + ")";
                }

                if (valueContainerType.getReferenceType() != null && valueContainerType.getReferenceType().isAbstract()) {
                    valueTypeCast = "(" + valueContainerType.getjPTypeName() + ")";
                }

                //object.put(key, value);
                builder.addStatement("object.put($Lkey, $Lvalue)", keyTypecast, valueTypeCast);
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

    private String getClassTypeForObjectMapperHandler(CodeGenSpec.Type type) {
        return getClassTypeForObjectMapperHandler(type, type.getjPTypeName());
    }

    private String getClassTypeForObjectMapperHandler(CodeGenSpec.Type type, TypeName jpTypeName) {
        if (CodeGenUtil.isCollectionOrMap(type.getNativeType())) {
            return "null";
        } else {
            return jpTypeName + ".class";
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

                if (fieldSpec.getName().equals("this")) {
                    // serializer.writeObjectAsBytes(0, handler.serialize(this));
                    builder.addStatement("serializer.writeObject($L, $LHandler, object, context)",
                            fieldSpec.getIndex(), fieldSpec.getName());
                } else {
                    // serializer.writeObjectAsBytes(0, handler.serialize(object.getaString()));
                    builder.addStatement("serializer.writeObject($L, $LHandler, object.$L$L, context)",
                            fieldSpec.getIndex(), fieldSpec.getName(), fieldSpec.getGetter(), handleGetterBean(fieldSpec));
                }

            } else if (isArray(fieldSpec)) {
                builder.beginControlFlow("for ($T val : object.$L$L)",
                        getReferenceTypeForContainers(realizedType, false), fieldSpec.getGetter(), handleGetterBean(fieldSpec));

                if (realizedType.getNativeType() == CodeGenSpec.DataType.OBJECT) {
                    builder.addStatement("serializer.writeObject($L, $LHandler, val, context)",
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
            return realizedType.getReferenceType().getJpClassName();
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
            // PibifyGenerated<List<List<String>>> handler = new InternalHandler2();
            builder.addStatement("PibifyGenerated<$L> $LHandler = HANDLER_MAP.get($S)",
                    fieldSpec.getType().getGenericTypeSignature(), fieldSpec.getName(), fieldSpec.getType().getGenericTypeSignature());

            if (fieldSpec.getName().equals("this")) {
                // serializer.writeObjectAsBytes(1, handler.serialize(object.getaString()));
                builder.addStatement("serializer.writeObject($L, $LHandler, object, context)", fieldSpec.getIndex(),
                        fieldSpec.getName());
            } else {
                // serializer.writeObjectAsBytes(1, handler.serialize(object.getaString()));
                builder.addStatement("serializer.writeObject($L, $LHandler, object.$L$L, context)", fieldSpec.getIndex(),
                        fieldSpec.getName(), fieldSpec.getGetter(), handleGetterBean(fieldSpec));
            }
        }
    }

    private void generateSerializerForObjectReference(CodeGenSpec.FieldSpec fieldSpec, MethodSpec.Builder builder) {
        if (fieldSpec.getType().getNativeType() != CodeGenSpec.DataType.OBJECT) {
            throw new IllegalArgumentException("generateSerializerForObjectReference invoked for non-reference types "
                    + fieldSpec.getName());
        }

        addHandlerForObjectReference(fieldSpec, builder, fieldSpec.getType().getReferenceType());
        builder.addStatement("serializer.writeObject($L, $LHandler, object.$L$L, context)", fieldSpec.getIndex(),
                fieldSpec.getName(), fieldSpec.getGetter(), handleGetterBean(fieldSpec));
    }

    private MethodSpec getDeserializer(ClassName thePojo, CodeGenSpec codeGenSpec) throws CodeGenException {
        try {
            MethodSpec.Builder builder = MethodSpec.methodBuilder("deserialize")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .returns(thePojo)
                    .addException(PibifyCodeExecException.class)
                    .addParameter(IDeserializer.class, "deserializer")
                    .addParameter(ParameterizedTypeName.get(ClassName.get(Class.class), thePojo), "clazz")
                    .addParameter(SerializationContext.class, "context")
                    .beginControlFlow("try")
                    .addStatement("int tag = deserializer.getNextTag()");

            handleObjectCreation(builder, thePojo, codeGenSpec);

            builder.beginControlFlow("while (tag != 0 && tag != PibifyGenerated.getEndObjectTag()) ")
                    .beginControlFlow("switch (tag) ");

            for (CodeGenSpec.FieldSpec fieldSpec : codeGenSpec.getFields()) {
                addFieldDeserializerBlock(fieldSpec, builder);
                builder.addStatement("$>$> break$<$<");
            }

            if (PibifyConfiguration.instance().ignoreUnknownFields()) {
                // TODO Add logger here
                builder.addStatement("default: break");
            } else {
                builder.addStatement("default: throw new UnsupportedOperationException($S + tag)", "Unable to find tag in gen code: ");
            }

            builder.endControlFlow()
                    .addStatement("tag = deserializer.getNextTag()")
                    .endControlFlow();

            handleObjectReturn(builder, codeGenSpec);

            builder.nextControlFlow("catch ($T e)", Exception.class)
                    .addStatement("throw new $T(e)", PibifyCodeExecException.class)
                    .endControlFlow();

            return builder.build();
        } catch (Exception e) {
            throw new CodeGenException(e.getMessage(), e);
        }
    }

    private void handleObjectReturn(MethodSpec.Builder builder, CodeGenSpec codeGenSpec) {
        if (!codeGenSpec.hasAllArgsConstructor()) {
            builder.addStatement("return object");
        } else {
            // handle array types
            for (CodeGenSpec.FieldSpec fieldSpec : codeGenSpec.getFields()) {
                if (CodeGenSpec.DataType.ARRAY.equals(fieldSpec.getType().getNativeType())) {

                    builder.addStatement("$L = new $T[$LList.size()]", fieldSpec.getName(),
                            fieldSpec.getType().getContainerTypes().get(0).getNativeType().getClazz() == null ?
                                    fieldSpec.getType().getContainerTypes().get(0).getjPTypeName() :
                                    fieldSpec.getType().getContainerTypes().get(0).getNativeType().getClazz(), fieldSpec.getName());
                    builder.addStatement("int i$L = 0", fieldSpec.getName());
                    builder.beginControlFlow("for ($T element : $LList)", fieldSpec.getType().getContainerTypes().get(0).getjPTypeName(), fieldSpec.getName())
                            .addStatement("$L[i$L++] = element", fieldSpec.getName(), fieldSpec.getName())
                            .endControlFlow();
                }
            }
            builder.addStatement("return new $T($L)", codeGenSpec.getJpClassName(),
                    String.join(", ", codeGenSpec.getFieldsInAllArgsConstructor()));
        }
    }

    private void handleObjectCreation(MethodSpec.Builder builder, ClassName thePojo, CodeGenSpec codeGenSpec) {
        // This method ensures that either we can create the deserialized object from empty constructor
        // or we add references for all fields, and use the allArgs constructor towards the end
        if (!codeGenSpec.hasAllArgsConstructor()) {
            builder.addStatement("$T object = new $T()", thePojo, thePojo);
        } else {
            Map<String, CodeGenSpec.FieldSpec> fieldSpecMap = codeGenSpec.getFields().stream()
                    .collect(Collectors.toMap(CodeGenSpec.FieldSpec::getName, Function.identity()));
            for (String fieldName : codeGenSpec.getFieldsInAllArgsConstructor()) {
                CodeGenSpec.Type type = fieldSpecMap.get(fieldName).getType();
                builder.addStatement("$T $L = null", type.getjPTypeName(), fieldName);
                if (CodeGenSpec.DataType.ARRAY.equals(type.getNativeType())) {
                    builder.addStatement("$T<$T> $LList = new $T()", List.class,
                            type.getContainerTypes().get(0).getjPTypeName(), fieldName, ArrayList.class);
                }
            }
        }
    }

    private void addDefaultSerializationBlock(CodeGenSpec.FieldSpec fieldSpec, MethodSpec.Builder builder) {
        // For strings, if it's configured to be a dictionary, write the string in the dictionary
        // and reference to the stream
        if (fieldSpec.isDictionary() &&
                isString(fieldSpec)) {
            builder.addStatement("serializer.writeInt($L, context.addStringToDictionary(object.$L$L))",
                    fieldSpec.getIndex(), fieldSpec.getGetter(), handleGetterBean(fieldSpec));
        } else {
            builder.addStatement("serializer.write$L($L, object.$L$L)",
                    fieldSpec.getType().getNativeType().getReadWriteMethodName(),
                    fieldSpec.getIndex(), fieldSpec.getGetter(),
                    handleGetterBean(fieldSpec));
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
                defaultDeserializationBlock(fieldSpec, builder);

                break;
        }
    }

    private void defaultDeserializationBlock(CodeGenSpec.FieldSpec fieldSpec, MethodSpec.Builder builder) throws InvalidPibifyAnnotation {
        String enumBlock = "", enumEndBlock = "";
        if (fieldSpec.getType().getNativeType() == CodeGenSpec.DataType.ENUM) {
            enumBlock = "getEnumValue(" + fieldSpec.getType().getReferenceType().getJpClassName() + ".values(),";
            enumEndBlock = ")";
        }

        if (fieldSpec.isDictionary() && isString(fieldSpec)) {
            if (fieldSpec.getSetter() != null) {
                builder.addStatement("case $L: \n $>" +
                                "object.$L$L($L context.getWord(deserializer.readInt()))$<",
                        TagPredictor.getTagBasedOnField(fieldSpec.getIndex(), int.class),
                        fieldSpec.getSetter(),
                        handleBeanSetter(fieldSpec),
                        getCastIfRequired(fieldSpec.getType().getNativeType()));
            } else {
                // case of all-args constructor
                builder.addStatement("case $L: \n $>" +
                                "$L = context.getWord(deserializer.readInt())$<",
                        TagPredictor.getTagBasedOnField(fieldSpec.getIndex(), int.class),
                        fieldSpec.getName()
                );
            }
        } else {
            if (!fieldSpec.useAllArgsConstructor()) {
                builder.addStatement("case $L: \n $>" +
                                "object.$L$L($L$L deserializer.read$L()$L)$<",
                        TagPredictor.getTagBasedOnField(fieldSpec.getIndex(), getClassForTag(fieldSpec)),
                        fieldSpec.getSetter(),
                        handleBeanSetter(fieldSpec),
                        enumBlock,
                        getCastIfRequired(fieldSpec.getType().getNativeType()),
                        fieldSpec.getType().getNativeType().getReadWriteMethodName(),
                        enumEndBlock
                );
            } else {
                // case of all-args constructor
                builder.addStatement("case $L: \n $>" +
                                "$L = $L$L deserializer.read$L()$L$<",
                        TagPredictor.getTagBasedOnField(fieldSpec.getIndex(), getClassForTag(fieldSpec)),
                        fieldSpec.getName(),
                        enumBlock,
                        getCastIfRequired(fieldSpec.getType().getNativeType()),
                        fieldSpec.getType().getNativeType().getReadWriteMethodName(),
                        enumEndBlock
                );
            }
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
        if (!fieldSpec.useAllArgsConstructor()) {
            builder.addStatement("object.$L$L($LHandler.deserialize(deserializer, context))",
                    fieldSpec.getSetter(), handleBeanSetter(fieldSpec), fieldSpec.getName());
        } else {
            // case of all-args constructor
            builder.addStatement("$L = $LHandler.deserialize(deserializer, context)",
                    fieldSpec.getName(), fieldSpec.getName());
        }

    }

    private void addCollectionDeserializer(CodeGenSpec.FieldSpec fieldSpec, MethodSpec.Builder builder) throws InvalidPibifyAnnotation {

        CodeGenSpec.Type realizedType = fieldSpec.getType().getContainerTypes().get(0);
        int tag = TagPredictor.getTagBasedOnField(fieldSpec.getIndex(), byte[].class);

        //PibifyGenerated<java.util.List<java.util.List<java.lang.String>>> aStringHandler = HANDLER_MAP.get("java.util.List<java.util.List<java.lang.String>>");
        builder.addStatement("case $L: \n$>" +
                        "$T<$L> $LHandler = HANDLER_MAP.get($S)", tag, PibifyGenerated.class,
                fieldSpec.getType().getGenericTypeSignature(),
                fieldSpec.getName(), fieldSpec.getType().getGenericTypeSignature());

        if (!fieldSpec.useAllArgsConstructor()) {
            builder.addStatement("$>$>object.$L$L($LHandler.deserialize(deserializer, context))$<$<",
                    fieldSpec.getSetter(), handleBeanSetter(fieldSpec), fieldSpec.getName());
        } else {
            // case of all-args constructor
            builder.addStatement("$>$>$L = $LHandler.deserialize(deserializer, context)$<$<",
                    fieldSpec.getName(), fieldSpec.getName());
        }
    }

    private void addArrayDeserializer(CodeGenSpec.FieldSpec fieldSpec, MethodSpec.Builder builder) throws InvalidPibifyAnnotation {
        CodeGenSpec.Type realizedType = fieldSpec.getType().getContainerTypes().get(0);

        if (realizedType.getNativeType() == CodeGenSpec.DataType.UNKNOWN) return;

        int tag = TagPredictor.getTagBasedOnField(fieldSpec.getIndex(), getClassForTag(fieldSpec));
        Object typeForContainers = getReferenceTypeForContainers(fieldSpec.getType().getContainerTypes().get(0), false);
        if (!fieldSpec.useAllArgsConstructor()) {
            builder.addStatement("case $L: \n$>$T[] newArray$L", tag, typeForContainers, tag)
                    .addStatement("$>$T[] oldArray$L = object.$L$L$<", typeForContainers, tag, fieldSpec.getGetter(), handleGetterBean(fieldSpec));
            if (realizedType.getNativeType() == CodeGenSpec.DataType.OBJECT) {
                CodeGenSpec refSpec = realizedType.getReferenceType();
                addHandlerForObjectReference(fieldSpec, builder, refSpec);

                builder.addStatement("$>$T val$L = $LHandler.deserialize(deserializer, $T.class, context)",
                        typeForContainers, tag, fieldSpec.getName(), typeForContainers);
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
                    .endControlFlow();

            builder.addStatement("$>object.$L$L(newArray$L)$<", fieldSpec.getSetter(), handleBeanSetter(fieldSpec), tag);
        } else {
            builder.addStatement("case $L: \n$>", tag);
            if (realizedType.getNativeType() == CodeGenSpec.DataType.OBJECT) {
                CodeGenSpec refSpec = realizedType.getReferenceType();
                addHandlerForObjectReference(fieldSpec, builder, refSpec);

                builder.addStatement("$>$T val$L = $LHandler.deserialize(deserializer, $T.class, context)",
                        typeForContainers, tag, fieldSpec.getName(), typeForContainers);
            } else {
                if (realizedType.getNativeType() == CodeGenSpec.DataType.ENUM) {
                    builder.addStatement("$>$T val$L = $T.values()[deserializer.readEnum()]$<",
                            typeForContainers, tag, typeForContainers);
                } else {
                    builder.addStatement("$>$T val$L = deserializer.read$L()$<",
                            typeForContainers, tag, realizedType.getNativeType().getReadWriteMethodName());
                }
            }
            builder.addStatement("$>$LList.add(val$L)$<", fieldSpec.getName(), tag);
        }
    }

    private void addObjectDeserializer(CodeGenSpec.FieldSpec fieldSpec, MethodSpec.Builder builder) throws InvalidPibifyAnnotation {
        CodeGenSpec refSpec = fieldSpec.getType().getReferenceType();
        int tag = TagPredictor.getTagBasedOnField(fieldSpec.getIndex(), byte[].class);
        builder.addStatement("case $L: \n$>", tag);
        addHandlerForObjectReference(fieldSpec.getName(), builder, refSpec);
        /* If the ref type is abstract, we won't have top level handlers for it. In such cases
         * it has to be treated as an object and let the PibifyObjectHandler resolve it
         * to the right concrete handler based on the runtime type of the object
         * */
        if (CodeGenUtil.isJavaLangObject(refSpec) || refSpec.isAbstract()) {

            ClassName referenceClass = refSpec.isAbstract() ? refSpec.getJpClassName() : ClassName.OBJECT;

            builder.addStatement("$>$T<$T,$T> $LEntry = (Map.Entry<String,$T>)($LHandler.deserialize(deserializer, context))$<",
                    Map.Entry.class, String.class, referenceClass, fieldSpec.getName(), referenceClass, fieldSpec.getName());
            if (!fieldSpec.useAllArgsConstructor()) {
                builder.addStatement("$>object.$L$L($LEntry.getValue())$<", fieldSpec.getSetter(), handleBeanSetter(fieldSpec), fieldSpec.getName());
            } else {
                // case of all-args constructor
                builder.addStatement("$>$L = $LEntry.getValue()$<", fieldSpec.getName(), fieldSpec.getName());
            }

        } else {
            if (!fieldSpec.useAllArgsConstructor()) {
                builder.addStatement("$>object.$L$L($LHandler.deserialize(deserializer, $T.class, context))$<",
                        fieldSpec.getSetter(), handleBeanSetter(fieldSpec), fieldSpec.getName(), refSpec.getJpClassName());
            } else {
                // case of all-args constructor
                builder.addStatement("$>$L = $LHandler.deserialize(deserializer, $T.class, context)$<",
                        fieldSpec.getName(), fieldSpec.getName(), refSpec.getJpClassName());
            }
        }
    }

    // This method takes care of public fields vs methods
    private String handleBeanSetter(CodeGenSpec.FieldSpec fieldSpec) {
        return fieldSpec.hasBeanMethods() ? "" : " = ";
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

    private void clearState() {
        classBuilder = null;
        fields.clear();
    }
}
