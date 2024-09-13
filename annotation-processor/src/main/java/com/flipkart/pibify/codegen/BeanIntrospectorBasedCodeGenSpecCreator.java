package com.flipkart.pibify.codegen;


import com.flipkart.pibify.core.Pibify;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;

import java.beans.BeanInfo;
import java.beans.FeatureDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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

    @Override
    public CodeGenSpec create(Class<?> type) throws CodeGenException {

        // cannot use computeIfAbsent because of checked exception being thrown

        if (!cache.containsKey(type)) {
            CodeGenSpec codeGenSpec = createImpl(type);
            handleSuperTypes(codeGenSpec, type);
            cache.put(type, codeGenSpec);
        }
        return cache.get(type);
    }

    private void handleSuperTypes(CodeGenSpec rootCodeGenSpec, Class<?> type) throws CodeGenException {
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

    private static CodeGenSpec getCodeGenSpec(Class<?> type) throws CodeGenException {
        String packageName;
        if (type.getEnclosingClass() == null) {
            packageName = type.getPackage().getName();
        } else {
            if (!Modifier.isStatic(type.getModifiers())) {
                throw new CodeGenException("Non-Static Inner classes are not supported: " + type.getCanonicalName());
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

            for (java.lang.reflect.Field reflectedField : type.getDeclaredFields()) {
                Pibify annotation = reflectedField.getAnnotation(Pibify.class);
                if (annotation != null) {
                    CodeGenSpec.FieldSpec fieldSpec = new CodeGenSpec.FieldSpec();
                    String name = reflectedField.getName();
                    fieldSpec.setIndex(annotation.value());
                    fieldSpec.setName(name);
                    fieldSpec.setType(getTypeFromJavaType(reflectedField.getName(), reflectedField.getGenericType(),
                            reflectedField.getType()));
                    fieldSpec.setGetter(namesToBeanInfo.get(name).getReadMethod().getName());
                    fieldSpec.setSetter(namesToBeanInfo.get(name).getWriteMethod().getName());
                    spec.addField(fieldSpec);

                    if (spec.getFields().size() >= MAX_FIELD_COUNT) {
                        throw new CodeGenException("Only " + MAX_FIELD_COUNT + " fields are supported per class");
                    }
                }
            }

            return spec;
        } catch (IntrospectionException e) {
            throw new CodeGenException(e.getMessage(), e);
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
                specType.setReferenceType(create(type));
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
            throw new UnsupportedOperationException(type.getSimpleName() +
                    " not supported in field " + fieldName);
        }
    }

    private CodeGenSpec.Type getUnknownType() {
        CodeGenSpec.Type type = new CodeGenSpec.Type();
        type.setNativeType(CodeGenSpec.DataType.UNKNOWN);
        return type;
    }

    private ClassName getNativeClassName(CodeGenSpec.Type specType) {
        // if specType is native, get the autoboxed class, else get the class from reference
        if (specType.getNativeType() == CodeGenSpec.DataType.OBJECT
                || specType.getNativeType() == CodeGenSpec.DataType.ENUM) {
            return ClassName.get(specType.getReferenceType().getPackageName(), specType.getReferenceType().getClassName());
        } else {
            return ClassName.get(specType.getNativeType().getAutoboxedClass());
        }
    }
}
