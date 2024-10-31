package com.flipkart.pibify.codegen;


import com.flipkart.pibify.codegen.log.CodeSpecGenLog;
import com.flipkart.pibify.codegen.log.FieldSpecGenLog;
import com.flipkart.pibify.codegen.log.SpecGenLog;
import com.flipkart.pibify.codegen.log.SpecGenLogLevel;
import com.flipkart.pibify.core.Pibify;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import org.apache.maven.plugin.logging.Log;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 * This class generates the CodeGenSpec based on incoming Class via Reflection
 * Author bageshwar.pn
 * Date 09/08/24
 */
public class BeanIntrospectorBasedCodeGenSpecCreator implements ICodeGenSpecCreator {

    public static final int MAX_FIELD_COUNT = 128;

    // to memoize results
    private final Map<Class<?>, CodeGenSpec> cache = new HashMap<>();

    private final Multimap<EntityUnderProcessing, SpecGenLog> logs = ArrayListMultimap.create();
    private final Stack<EntityUnderProcessing> stackOfUnderProcessing = new Stack<>();
    private EntityUnderProcessing underProcessing;
    private final Log mavenLogger;

    public BeanIntrospectorBasedCodeGenSpecCreator(Log logger) {
        this.mavenLogger = logger;
    }

    public BeanIntrospectorBasedCodeGenSpecCreator() {
        this(null);
    }

    @Override
    public CodeGenSpec create(Class<?> type) throws CodeGenException {

        try {
            if (underProcessing != null) {
                stackOfUnderProcessing.push(underProcessing);
            }
            underProcessing = new EntityUnderProcessing(type);

            // cannot use computeIfAbsent because of checked exception being thrown
            if (!cache.containsKey(type)) {
                CodeGenSpec codeGenSpec = createImpl(type);
                handleSuperTypes(codeGenSpec, type);
                handleCollectionOrMap(codeGenSpec, type);
                cache.put(type, codeGenSpec);
            }

            return cache.get(type);
        } finally {
            if (!stackOfUnderProcessing.empty()) {
                underProcessing = stackOfUnderProcessing.pop();
            }
        }
    }

    private void handleCollectionOrMap(CodeGenSpec codeGenSpec, Class<?> type) throws CodeGenException {
        if (type.equals(Object.class)) {
            return;
        }

        try {
            // find the index of the last field and use that to identify the next shift value
            // for collection/map add 1/2 fields to write the (key and) value at that index
            int index = MAX_FIELD_COUNT;
            if (!codeGenSpec.getFields().isEmpty()) {
                index = codeGenSpec.getFields().get(codeGenSpec.getFields().size() - 1).getIndex();
                int hCount = index / MAX_FIELD_COUNT;
                index = (hCount + 1) * MAX_FIELD_COUNT;
            }


            if (Collection.class.isAssignableFrom(type)) {
                CodeGenSpec.FieldSpec fieldSpec = new CodeGenSpec.FieldSpec();
                fieldSpec.setIndex(index++);
                fieldSpec.setName("this");
                fieldSpec.setType(getTypeFromJavaType("this", type.getGenericSuperclass(), type, true));
                fieldSpec.setGetter("");
                fieldSpec.setSetter("addAll");
                codeGenSpec.addField(fieldSpec);
            } else if (Map.class.isAssignableFrom(type)) {
                CodeGenSpec.FieldSpec fieldSpec = new CodeGenSpec.FieldSpec();
                fieldSpec.setIndex(index++);
                fieldSpec.setName("this");
                fieldSpec.setType(getTypeFromJavaType("this", type.getGenericSuperclass(), type, true));
                fieldSpec.setGetter("");
                fieldSpec.setSetter("putAll");
                codeGenSpec.addField(fieldSpec);
            } else {
                // ignore
            }
        } catch (Exception e) {
            throw new CodeGenException(e.getMessage(), e);
        }
    }

    @Override
    public void resetState() {
        cache.clear();
        underProcessing = null;
        logs.clear();
    }


    @Override
    public Collection<SpecGenLog> getLogsForCurrentEntity() {
        return logs.get(underProcessing);
    }

    @Override
    public Collection<SpecGenLog> getLogsForCurrentEntity(Class<?> entity) {
        return logs.get(EntityUnderProcessing.of(entity));
    }

    @Override
    public SpecGenLogLevel status(Class<?> entity) {
        SpecGenLogLevel level = SpecGenLogLevel.INFO;

        for (SpecGenLog value : logs.get(EntityUnderProcessing.of(entity))) {
            if (value.getLogLevel().ordinal() > level.ordinal()) {
                level = value.getLogLevel();
            }
        }

        return level;
    }

    private void log(SpecGenLog spec) {
        spec.prependMessage(underProcessing.getFqdn());
        logs.put(underProcessing, spec);
        if (mavenLogger != null) {
            switch (spec.getLogLevel()) {
                case ERROR:
                    mavenLogger.error("CodeSpecCreator: " + spec.getLogMessage());
                    break;
                case WARN:
                    mavenLogger.warn("CodeSpecCreator: " + spec.getLogMessage());
                    break;
                case INFO:
                default:
                    mavenLogger.info("CodeSpecCreator: " + spec.getLogMessage());
                    break;
            }
        }
    }

    private void handleSuperTypes(CodeGenSpec rootCodeGenSpec, Class<?> type) throws CodeGenException {
        // This short-circuit is needed to handle reference types which have been type-erased and interfaces.
        if (type.equals(Object.class) || type.isInterface()) {
            return;
        }

        Class<?> sType = type.getSuperclass();
        int shiftBy = MAX_FIELD_COUNT;
        while (!Object.class.equals(sType)) {
            CodeGenSpec codeGenSpec = createImpl(sType);
            for (CodeGenSpec.FieldSpec field : codeGenSpec.getFields()) {
                field.setIndex(field.getIndex() + shiftBy);
                rootCodeGenSpec.getFields().add(field);
            }
            sType = sType.getSuperclass();
            shiftBy = shiftBy + MAX_FIELD_COUNT;
        }
    }

    private CodeGenSpec getCodeGenSpec(Class<?> type) throws CodeGenException {
        String packageName;
        if (type.getEnclosingClass() == null) {
            packageName = type.getPackage().getName();
        } else {
            if (!Modifier.isStatic(type.getModifiers())) {
                log(new CodeSpecGenLog(SpecGenLogLevel.ERROR, "Non-Static Inner classes are not supported: " + type.getCanonicalName()));
            }
            packageName = type.getEnclosingClass().getName();
        }

        return new CodeGenSpec(packageName, type.getSimpleName(), (type.getEnclosingClass() != null));
    }

    private CodeGenSpec createImpl(Class<?> type) throws CodeGenException {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(type);

            Map<CaseInsensitiveString, PropertyDescriptor> namesToBeanInfo = new HashMap<>();
            for (PropertyDescriptor f : beanInfo.getPropertyDescriptors()) {
                if (namesToBeanInfo.put(CaseInsensitiveString.of(f.getName()), f) != null) {
                    throw new IllegalStateException("Duplicate key");
                }
            }

            CodeGenSpec spec = getCodeGenSpec(type);

            // If the class under processing is an enum, there is no need of a codespec.
            // enum cardinals are directly used in pibify
            // To be revisited!
            if (type.isEnum()) {
                return spec;
            }

            if (Modifier.isAbstract(type.getModifiers())) {
                spec.setAbstract(true);
            }

            Map<Integer, CodeGenSpec.FieldSpec> mapOfFields = new HashMap<>();
            // This set is used to find duplicate field names (case-insensitive)
            Set<CaseInsensitiveString> fieldNameSet = new HashSet<>();

            for (java.lang.reflect.Field reflectedField : type.getDeclaredFields()) {
                CaseInsensitiveString name = CaseInsensitiveString.of(reflectedField.getName());

                if (fieldNameSet.contains(name)) {
                    // We cannot have duplicate field names, because it messes up with the getters
                    log(new FieldSpecGenLog(reflectedField, SpecGenLogLevel.ERROR, "Duplicate field"));
                    break;
                } else {
                    fieldNameSet.add(name);
                }

                Pibify annotation = reflectedField.getAnnotation(Pibify.class);
                if (annotation != null) {
                    validatePibifyAnnotation(reflectedField, annotation);

                    CodeGenSpec.FieldSpec fieldSpec = new CodeGenSpec.FieldSpec();

                    // Validating duplicate fields
                    if (mapOfFields.containsKey(annotation.value())) {
                        log(new FieldSpecGenLog(reflectedField, SpecGenLogLevel.ERROR, "Field with duplicate index: " +
                                mapOfFields.get(annotation.value()).getName()));
                    } else {
                        mapOfFields.put(annotation.value(), fieldSpec);
                    }

                    fieldSpec.setIndex(annotation.value());
                    fieldSpec.setName(name.getString());
                    this.underProcessing.setReflectedField(reflectedField);
                    fieldSpec.setType(getTypeFromJavaType(reflectedField.getName(), reflectedField.getGenericType(),
                            reflectedField.getType()));

                    if (!namesToBeanInfo.containsKey(name)) {
                        // if we have a public field, use that instead of beanInfo
                        if (Modifier.isPublic(reflectedField.getModifiers())) {
                            fieldSpec.setGetter(name.getString());
                            fieldSpec.setSetter(name.getString());
                            fieldSpec.setHasBeanMethods(false);
                        } else {
                            log(new FieldSpecGenLog(reflectedField, SpecGenLogLevel.ERROR, "BeanInfo missing"));
                            continue;
                        }
                    } else {
                        if (namesToBeanInfo.get(name).getReadMethod() == null
                                || namesToBeanInfo.get(name).getWriteMethod() == null) {
                            log(new FieldSpecGenLog(reflectedField, SpecGenLogLevel.ERROR, "Setter/getter missing"));
                            continue;
                        } else {
                            fieldSpec.setGetter(namesToBeanInfo.get(name).getReadMethod().getName());
                            fieldSpec.setSetter(namesToBeanInfo.get(name).getWriteMethod().getName());
                        }
                    }

                    spec.addField(fieldSpec);

                    if (spec.getFields().size() >= MAX_FIELD_COUNT) {
                        log(new CodeSpecGenLog(SpecGenLogLevel.ERROR, "Only " + MAX_FIELD_COUNT + " fields are supported per class"));
                    }
                }
            }

            return spec;
        } catch (IntrospectionException e) {
            throw new CodeGenException(e.getMessage(), e);
        }
    }

    private void validatePibifyAnnotation(Field field, Pibify annotation) {

        if (annotation.value() >= BeanIntrospectorBasedCodeGenSpecCreator.MAX_FIELD_COUNT) {
            log(new FieldSpecGenLog(field, SpecGenLogLevel.ERROR, "Index cannot be more than " + MAX_FIELD_COUNT));
        }

        if (annotation.value() <= 0) {
            log(new FieldSpecGenLog(field, SpecGenLogLevel.ERROR, "Index cannot be less than or equal to 0"));
        }
    }

    private CodeGenSpec.Type getTypeFromJavaType(String fieldName, Type fieldGenericType, Class<?> type) throws CodeGenException {
        return getTypeFromJavaType(fieldName, fieldGenericType, type, false);
    }

    private CodeGenSpec.Type getTypeFromJavaType(String fieldName, Type fieldGenericType, Class<?> type, boolean noPibify) throws CodeGenException {
        CodeGenSpec.Type specType = new CodeGenSpec.Type();
        CodeGenSpec.DataType nativeType = CodeSpecMeta.CLASS_TO_TYPE_MAP.get(type);

        if (nativeType != null) {
            specType.setNativeType(nativeType);
            specType.setjPTypeName(getNativeClassName(specType));
        } else {
            specType.setContainerTypes(new ArrayList<>(2));
            if (type.isArray()) {
                Class<?> arrayType = type.getComponentType();
                specType.setNativeType(getNativeArrayType(arrayType));
                specType.getContainerTypes().add(getTypeFromJavaType(fieldName, fieldGenericType, arrayType));

                specType.setCollectionType(getCollectionType(type));

                // TO avoid autoboxing primitive arrays
                if (arrayType.isPrimitive()) {
                    specType.setjPTypeName(ArrayTypeName.of(arrayType));
                    specType.setGenericTypeSignature(arrayType.getTypeName() + "[]");
                } else {
                    specType.setjPTypeName(ArrayTypeName.of(specType.getContainerTypes().get(0).getjPTypeName()));
                    specType.setGenericTypeSignature(specType.getContainerTypes().get(0).getGenericTypeSignature() + "[]");
                }

            } else if (Collection.class.isAssignableFrom(type)) {

                // If this is not an interface reference type, capture the actual type
                // to be used for typecasting during serde
                if (!(type.equals(List.class) || type.equals(Set.class)) && !noPibify) {
                    if (fieldGenericType instanceof TypeVariable) {
                        // if this is generic type reference, try and extract its actual type
                        Field field = underProcessing.getReflectedField();
                        Class<?> determinedType = CodeGenUtil.determineType(field.getGenericType(), field.getDeclaringClass(), underProcessing.getType());
                        specType.setReferenceType(create(determinedType));
                    } else {
                        specType.setReferenceType(create(type));
                    }
                }

                if (!CodeGenUtil.isNonPibifyClass(type) && !noPibify) {
                    specType.setNativeType(CodeGenSpec.DataType.OBJECT);
                    // release the object, since it's not needed
                    specType.setContainerTypes(null);
                    specType.setjPTypeName(getNativeClassName(specType));
                } else {
                    specType.setNativeType(CodeGenSpec.DataType.COLLECTION);
                    specType.getContainerTypes().add(getContainerType(fieldName, fieldGenericType, type));
                    specType.setCollectionType(getCollectionType(type));

                    setJavaPoetMetaObjects(type, specType, ClassName.get(specType.getCollectionType().getInterfaceClass()),
                            ClassName.get(specType.getCollectionType().getImplementationClass()),
                            fieldGenericType,
                            specType.getContainerTypes().get(0).getjPTypeName());
                }
            } else if (Map.class.isAssignableFrom(type)) {

                // If this is not an interface reference type, capture the actual type
                // to be used for typecasting during serde
                if (!type.equals(Map.class) && !noPibify) {
                    if (fieldGenericType instanceof TypeVariable) {
                        // if this is generic type reference, try and extract its actual type
                        Field field = underProcessing.getReflectedField();
                        Class<?> determinedType = CodeGenUtil.determineType(field.getGenericType(), field.getDeclaringClass(), underProcessing.getType());
                        specType.setReferenceType(create(determinedType));
                    } else {
                        specType.setReferenceType(create(type));
                    }
                }

                if (!CodeGenUtil.isNonPibifyClass(type) && !noPibify) {
                    // the class is a non-pibify map
                    // embed the object creator macro
                    specType.setNativeType(CodeGenSpec.DataType.OBJECT);
                    // release the object, since it's not needed
                    specType.setContainerTypes(null);

                    if (specType.getReferenceType() != null) {
                        specType.setjPTypeName(getNativeClassName(specType));
                    }
                } else {
                    specType.setNativeType(CodeGenSpec.DataType.MAP);
                    specType.getContainerTypes().add(getContainerType(fieldName, fieldGenericType, type, 0));
                    specType.getContainerTypes().add(getContainerType(fieldName, fieldGenericType, type, 1));

                    setJavaPoetMetaObjects(type, specType, ClassName.get(Map.class), ClassName.get(HashMap.class),
                            fieldGenericType,
                            specType.getContainerTypes().get(0).getjPTypeName(),
                            specType.getContainerTypes().get(1).getjPTypeName());
                    // TBF
                    /*specType.setjPTypeName(ParameterizedTypeName.get(ClassName.get(Map.class),
                            specType.getContainerTypes().get(0).getjPTypeName(),
                            specType.getContainerTypes().get(1).getjPTypeName()));*/
                }
            } else if (Enum.class.isAssignableFrom(type)) {
                specType.setNativeType(CodeGenSpec.DataType.ENUM);
                specType.setContainerTypes(null);
                specType.setReferenceType(create(type));
                specType.setjPTypeName(getNativeClassName(specType));
            } else {
                typeForObjectType(fieldGenericType, type, specType);
            }
        }

        if (!type.isArray()) {
            specType.setGenericTypeSignature(specType.getjPTypeName().toString());
        }

        return specType;
    }

    private void setJavaPoetMetaObjects(Class<?> type, CodeGenSpec.Type specType, ClassName interfaceClass,
                                        ClassName defaultImplementationType, Type fieldGenericType, TypeName... parameterizedTypeParams) {
        TypeName jpTypeName;
        if (specType.getReferenceType() != null) {
            if (type.getTypeParameters().length != 0) {
                // since jpTypes are used to create field/method signatures
                // picking the type params of containers from the fieldGenericType of the reference.
                TypeName[] types = Arrays.stream(((ParameterizedType) fieldGenericType).getActualTypeArguments()).map(TypeName::get).toArray(TypeName[]::new);
                jpTypeName = ParameterizedTypeName.get(specType.getReferenceType().getJpClassName(), types);
            } else {
                jpTypeName = specType.getReferenceType().getJpClassName();
            }

            // if there is a different reference type for this collection
            // use that to create new instances if possible
            if (specType.getReferenceType().isAbstract()) {
                log(new CodeSpecGenLog(SpecGenLogLevel.ERROR, "Abstract subclasses of Collection not supported"));
                // Still go ahead and set dummy implementation
                specType.setNewInstanceType(defaultImplementationType);
            } else {
                specType.setNewInstanceType(specType.getReferenceType().getJpClassName());
            }
        } else {
            jpTypeName = ParameterizedTypeName.get(interfaceClass, parameterizedTypeParams);
            // If we don't have a different reference type, use the default implementation types of collections.
            // i.e. if reference type is a List/Set etc., use new ArrayList() or new HashSet() when creating instances.
            specType.setNewInstanceType(defaultImplementationType);
        }

        specType.setjPTypeName(jpTypeName);
    }

    private void typeForObjectType(Type fieldGenericType, Class<?> type, CodeGenSpec.Type specType) throws CodeGenException {
        specType.setNativeType(CodeGenSpec.DataType.OBJECT);
        // release the object, since it's not needed
        specType.setContainerTypes(null);
        if (fieldGenericType instanceof TypeVariable) {
            // if this is generic type reference, try and extract its actual type
            Field field = underProcessing.getReflectedField();
            Class<?> determinedType = CodeGenUtil.determineType(field.getGenericType(), field.getDeclaringClass(), underProcessing.getType());
            specType.setReferenceType(create(determinedType));
        } else {
            specType.setReferenceType(create(type));
        }

        specType.setjPTypeName(getNativeClassName(specType));
    }

    private CodeGenSpec.DataType getNativeArrayType(Class<?> arrayType) {
        if (byte.class.equals(arrayType)) {
            return CodeGenSpec.DataType.BYTE_ARRAY;
        } else {
            return CodeGenSpec.DataType.ARRAY;
        }
    }

    private CodeGenSpec.CollectionType getCollectionType(Class<?> type) {
        if (List.class.isAssignableFrom(type)) {
            return CodeGenSpec.CollectionType.LIST;
        } else if (Set.class.isAssignableFrom(type)) {
            return CodeGenSpec.CollectionType.SET;
        } else if (Queue.class.isAssignableFrom(type)) {
            return CodeGenSpec.CollectionType.QUEUE;
        } else if (Deque.class.isAssignableFrom(type)) {
            return CodeGenSpec.CollectionType.DEQUE;
        } else {
            // Default collections to List!
            return CodeGenSpec.CollectionType.LIST;
        }
    }

    private CodeGenSpec.Type getContainerType(String fieldName, Type fieldGenericType, Class<?> type) throws CodeGenException {
        return getContainerType(fieldName, fieldGenericType, type, 0);
    }

    private CodeGenSpec.Type getContainerType(String fieldName, Type fieldGenericType, Class<?> type, int index) throws CodeGenException {
        ParameterizedType genericType = null;

        if (Object.class.equals(fieldGenericType)) {
            // We have reached the end of the tunnel
            // this usually happens for type-erased containers
            return getTypeFromJavaType(fieldName, fieldGenericType, Object.class);
        }

        if (fieldGenericType instanceof ParameterizedType) {
            genericType = (ParameterizedType) fieldGenericType;
        }
        // If we are processing a map, it's possible that there are
        // subclasses with part of the type parameter specified.
        // In those cases, try and get the parameter from superclass
        if (Map.class.isAssignableFrom(type) &&
                (genericType == null || genericType.getActualTypeArguments().length != 2)) {
            Class<?> typeInHierarchy = type;
            do {
                genericType = (ParameterizedType) typeInHierarchy.getGenericSuperclass();
                typeInHierarchy = typeInHierarchy.getSuperclass();
            } while (genericType.getActualTypeArguments().length != 2);
        } else if (Collection.class.isAssignableFrom(type) &&
                (genericType == null || genericType.getActualTypeArguments().length != 1)) {
            Class<?> typeInHierarchy = type;
            do {
                genericType = (ParameterizedType) typeInHierarchy.getGenericSuperclass();
                typeInHierarchy = typeInHierarchy.getSuperclass();
            } while (genericType.getActualTypeArguments().length != 1);
        }

        Type actualTypeArgument = genericType.getActualTypeArguments()[index];
        if (actualTypeArgument instanceof Class) {
            Class<?> typeParamClass = (Class<?>) actualTypeArgument;
            return getTypeFromJavaType(fieldName, null, typeParamClass);
        } else if (actualTypeArgument instanceof WildcardType) {
            return getUnknownType();
        } else if (actualTypeArgument instanceof ParameterizedType) {
            ParameterizedType castedActualType = (ParameterizedType) actualTypeArgument;
            return getTypeFromJavaType(fieldName, actualTypeArgument, (Class<?>) castedActualType.getRawType());
        } else {
            actualTypeArgument = CodeGenUtil.resolveTypeViaSuperClassHierarchy(actualTypeArgument, (ParameterizedType) fieldGenericType, genericType, index);
            if (actualTypeArgument instanceof Class) {
                Class<?> typeParamClass = (Class<?>) actualTypeArgument;
                return getTypeFromJavaType(fieldName, fieldGenericType, typeParamClass);
            }

            if (underProcessing.getReflectedField() == null) {
                // return Object as the type if we cannot resolve the actual type
                // then the PibifyObjectHandler takes care of marshalling
                return getTypeFromJavaType(fieldName, null, Object.class);
            }

            Type resolvedType = CodeGenUtil.getType(underProcessing.getType(), actualTypeArgument, underProcessing.getReflectedField().getDeclaringClass()).type;
            if (resolvedType instanceof Class) {
                // passing the generic type as null because it has been resolved at this level.
                return getTypeFromJavaType(fieldName, null, (Class<?>) resolvedType);
            } else {
                // return Object as the type if we cannot resolve the actual type
                // then the PibifyObjectHandler takes care of marshalling
                return getTypeFromJavaType(fieldName, null, Object.class);
            }
        }
    }

    private CodeGenSpec.Type getUnknownType() {
        CodeGenSpec.Type type = new CodeGenSpec.Type();
        type.setNativeType(CodeGenSpec.DataType.UNKNOWN);
        return type;
    }

    private ClassName getNativeClassName(CodeGenSpec.Type specType) {
        // if specType is native, get the auto-boxed class, else get the class from reference
        if (specType.getNativeType() == CodeGenSpec.DataType.OBJECT
                || specType.getNativeType() == CodeGenSpec.DataType.ENUM) {
            return ClassName.get(specType.getReferenceType().getPackageName(), specType.getReferenceType().getClassName());
        } else {
            return ClassName.get(specType.getNativeType().getAutoboxedClass());
        }
    }
}
