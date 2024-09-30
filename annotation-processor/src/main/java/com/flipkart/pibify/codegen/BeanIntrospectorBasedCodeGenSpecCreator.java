package com.flipkart.pibify.codegen;


import com.flipkart.pibify.codegen.log.CodeSpecGenLog;
import com.flipkart.pibify.codegen.log.FieldSpecGenLog;
import com.flipkart.pibify.codegen.log.SpecGenLog;
import com.flipkart.pibify.codegen.log.SpecGenLogLevel;
import com.flipkart.pibify.core.Pibify;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;

import java.beans.BeanInfo;
import java.beans.FeatureDescriptor;
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
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

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

    @Override
    public CodeGenSpec create(Class<?> type) throws CodeGenException {

        if (underProcessing != null) {
            stackOfUnderProcessing.push(underProcessing);
        }
        underProcessing = new EntityUnderProcessing(type, type.getCanonicalName());

        // cannot use computeIfAbsent because of checked exception being thrown
        if (!cache.containsKey(type)) {
            CodeGenSpec codeGenSpec = createImpl(type);
            handleSuperTypes(codeGenSpec, type);
            cache.put(type, codeGenSpec);
        }

        if (!stackOfUnderProcessing.empty()) {
            underProcessing = stackOfUnderProcessing.pop();
        }

        return cache.get(type);
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
    public SpecGenLogLevel status() {
        SpecGenLogLevel level = SpecGenLogLevel.INFO;

        for (SpecGenLog value : logs.values()) {
            if (value.getLogLevel().ordinal() > level.ordinal()) {
                level = value.getLogLevel();
            }
        }

        return level;
    }

    private void log(SpecGenLog spec) {
        spec.prependMessage(underProcessing.getFqdn());
        logs.put(underProcessing, spec);
    }

    private void handleSuperTypes(CodeGenSpec rootCodeGenSpec, Class<?> type) throws CodeGenException {
        // This short-circuit is needed to handle reference types which have been type-erased.
        if (type.equals(Object.class)) {
            return;
        }

        Class<?> sType = type.getSuperclass();
        int shiftBy = MAX_FIELD_COUNT;
        while (!sType.equals(Object.class)) {
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

            Map<String, PropertyDescriptor> namesToBeanInfo = Arrays.stream(beanInfo.getPropertyDescriptors())
                    .collect(Collectors.toMap(FeatureDescriptor::getName, f -> f));

            CodeGenSpec spec = getCodeGenSpec(type);

            Map<Integer, CodeGenSpec.FieldSpec> mapOfFields = new HashMap<>();

            for (java.lang.reflect.Field reflectedField : type.getDeclaredFields()) {
                Pibify annotation = reflectedField.getAnnotation(Pibify.class);
                if (annotation != null) {
                    validatePibifyAnnotation(reflectedField, annotation);
                    String name = reflectedField.getName();

                    CodeGenSpec.FieldSpec fieldSpec = new CodeGenSpec.FieldSpec();

                    // Validating duplicate fields
                    if (mapOfFields.containsKey(annotation.value())) {
                        log(new FieldSpecGenLog(reflectedField, SpecGenLogLevel.ERROR, "Field with duplicate index: " +
                                mapOfFields.get(annotation.value()).getName()));
                    } else {
                        mapOfFields.put(annotation.value(), fieldSpec);
                    }

                    fieldSpec.setIndex(annotation.value());
                    fieldSpec.setName(name);
                    this.underProcessing.setReflectedField(reflectedField);
                    fieldSpec.setType(getTypeFromJavaType(reflectedField.getName(), reflectedField.getGenericType(),
                            reflectedField.getType()));

                    if (!namesToBeanInfo.containsKey(name)) {
                        // hack for bean nomenclature for lombok classes
                        // Lombok generates UpperCamelCase instead of camelCase
                        // for fields where the size of the first word (lower-cased) is 1.
                        // e.g for anApple, getter is AnApple and fieldName tagged is anApple
                        // but for aMango, getter is AMango and fieldName is AMango instead of aMango
                        if (Character.isLowerCase(name.charAt(0)) && Character.isUpperCase(name.charAt(1))) {
                            name.toCharArray()[0] = Character.toUpperCase(name.charAt(0));
                            log(new FieldSpecGenLog(reflectedField, SpecGenLogLevel.INFO, "Renamed field to " + name));
                        }

                        if (!namesToBeanInfo.containsKey(name)) {
                            log(new FieldSpecGenLog(reflectedField, SpecGenLogLevel.ERROR, "BeanInfo missing"));
                            continue;
                        }
                    }

                    fieldSpec.setGetter(namesToBeanInfo.get(name).getReadMethod().getName());
                    fieldSpec.setSetter(namesToBeanInfo.get(name).getWriteMethod().getName());
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
        CodeGenSpec.Type specType = new CodeGenSpec.Type();
        CodeGenSpec.DataType nativeType = CodeSpecMeta.CLASS_TO_TYPE_MAP.get(type);

        if (nativeType != null) {
            specType.setNativeType(nativeType);
            specType.setjPTypeName(getNativeClassName(specType));
        } else {
            specType.setContainerTypes(new ArrayList<>(2));
            if (type.isArray()) {
                Class<?> arrayType = type.getComponentType();
                specType.setNativeType(CodeGenSpec.DataType.ARRAY);
                specType.getContainerTypes().add(getTypeFromJavaType(fieldName, fieldGenericType, arrayType));
            } else if (Collection.class.isAssignableFrom(type)) {
                specType.setNativeType(CodeGenSpec.DataType.COLLECTION);
                specType.getContainerTypes().add(getContainerType(fieldName, fieldGenericType, type));
                specType.setCollectionType(getCollectionType(type));
                specType.setjPTypeName(ParameterizedTypeName.get(ClassName.get(specType.getCollectionType().getInterfaceClass()),
                        specType.getContainerTypes().get(0).getjPTypeName()));
            } else if (Map.class.isAssignableFrom(type)) {
                specType.setNativeType(CodeGenSpec.DataType.MAP);
                specType.getContainerTypes().add(getContainerType(fieldName, fieldGenericType, type, 0));
                specType.getContainerTypes().add(getContainerType(fieldName, fieldGenericType, type, 1));
                specType.setjPTypeName(ParameterizedTypeName.get(ClassName.get(Map.class),
                        specType.getContainerTypes().get(0).getjPTypeName(),
                        specType.getContainerTypes().get(1).getjPTypeName()));
            } else if (Enum.class.isAssignableFrom(type)) {
                specType.setNativeType(CodeGenSpec.DataType.ENUM);
                specType.setContainerTypes(null);
                specType.setReferenceType(create(type));
                specType.setjPTypeName(getNativeClassName(specType));
            } else {
                specType.setNativeType(CodeGenSpec.DataType.OBJECT);
                // release the object, since it's not needed
                specType.setContainerTypes(null);
                if (fieldGenericType instanceof TypeVariable) {
                    // if this is generic type reference, try and extract its actual type
                    Class<?> determinedType = CodeGenUtil.determineType(underProcessing.getReflectedField(), underProcessing.getType());
                    specType.setReferenceType(create(determinedType));
                } else {
                    specType.setReferenceType(create(type));
                }

                specType.setjPTypeName(getNativeClassName(specType));
            }
        }

        if (!CodeGenUtil.isArray(specType.getNativeType())) {
            specType.setGenericTypeSignature(CodeGenUtil.getGenericTypeStringForField(specType));
        }

        return specType;
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
        ParameterizedType genericType = (ParameterizedType) fieldGenericType;
        Type actualTypeArgument = genericType.getActualTypeArguments()[index];
        if (actualTypeArgument instanceof Class) {
            Class<?> typeParamClass = (Class<?>) actualTypeArgument;
            return getTypeFromJavaType(fieldName, fieldGenericType, typeParamClass);
        } else if (actualTypeArgument instanceof WildcardType) {
            return getUnknownType();
        } else if (actualTypeArgument instanceof ParameterizedType) {
            ParameterizedType castedActualType = (ParameterizedType) actualTypeArgument;
            return getTypeFromJavaType(fieldName, actualTypeArgument, (Class<?>) castedActualType.getRawType());
        } else {
            log(new FieldSpecGenLog(fieldName, SpecGenLogLevel.ERROR, type.getSimpleName() + " not supported in field " + fieldName));
            return null;
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
