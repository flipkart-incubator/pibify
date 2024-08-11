package com.flipkart.pibify.codegen;


import com.flipkart.pibify.core.Pibify;

import java.beans.BeanInfo;
import java.beans.FeatureDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
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

    // to memoize results
    private final Map<Class<?>, CodeGenSpec> cache = new HashMap<>();

    @Override
    public CodeGenSpec create(Class<?> type) throws CodeGenException {

        // cannot use computeIfAbsent because of checked exception being thrown

        if (!cache.containsKey(type)) {
            CodeGenSpec codeGenSpec = createImpl(type);
            cache.put(type, codeGenSpec);
        }
        return cache.get(type);
    }

    private CodeGenSpec createImpl(Class<?> type) throws CodeGenException {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(type);

            Map<String, PropertyDescriptor> namesToBeanInfo = Arrays.stream(beanInfo.getPropertyDescriptors())
                    .collect(Collectors.toMap(FeatureDescriptor::getName, f -> f));

            CodeGenSpec spec = new CodeGenSpec(type.getPackage().getName(), type.getSimpleName());

            for (java.lang.reflect.Field reflectedField : type.getDeclaredFields()) {
                Pibify annotation = reflectedField.getAnnotation(Pibify.class);
                if (annotation != null) {
                    CodeGenSpec.FieldSpec fieldSpec = new CodeGenSpec.FieldSpec();
                    String name = reflectedField.getName();
                    fieldSpec.setIndex(annotation.value());
                    fieldSpec.setName(name);
                    fieldSpec.setType(getTypeFromJavaType(reflectedField, reflectedField.getType()));
                    fieldSpec.setGetter(namesToBeanInfo.get(name).getReadMethod().getName());
                    fieldSpec.setSetter(namesToBeanInfo.get(name).getWriteMethod().getName());
                    spec.addField(fieldSpec);
                }
            }

            return spec;
        } catch (IntrospectionException e) {
            throw new CodeGenException(e.getMessage(), e);
        }
    }

    private CodeGenSpec.Type getTypeFromJavaType(Field reflectedField, Class<?> type) throws CodeGenException {
        CodeGenSpec.Type specType = new CodeGenSpec.Type();
        CodeGenSpec.DataType nativeType = CodeSpecMeta.CLASS_TO_TYPE_MAP.get(type);

        if (nativeType != null) {
            specType.nativeType = nativeType;
        } else {
            specType.containerTypes = new ArrayList<>(2);
            if (type.isArray()) {
                Class<?> arrayType = type.getComponentType();
                specType.nativeType = CodeGenSpec.DataType.ARRAY;
                specType.containerTypes.add(getTypeFromJavaType(reflectedField, arrayType));
            } else if (Collection.class.isAssignableFrom(type)) {
                specType.nativeType = CodeGenSpec.DataType.COLLECTION;
                specType.containerTypes.add(getContainerType(reflectedField, type));
                specType.collectionType = getCollectionType(type);
            } else if (Map.class.isAssignableFrom(type)) {
                specType.nativeType = CodeGenSpec.DataType.MAP;
                specType.containerTypes.add(getContainerType(reflectedField, type, 0));
                specType.containerTypes.add(getContainerType(reflectedField, type, 1));
            } else {
                specType.nativeType = CodeGenSpec.DataType.OBJECT;
                // release the object, since its not needed
                specType.containerTypes = null;
                specType.referenceType = create(type);
            }
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

    private CodeGenSpec.Type getContainerType(Field reflectedField, Class<?> type) throws CodeGenException {
        return getContainerType(reflectedField, type, 0);
    }

    private CodeGenSpec.Type getContainerType(Field reflectedField, Class<?> type, int index) throws CodeGenException {
        ParameterizedType genericType = (ParameterizedType) reflectedField.getGenericType();
        Type actualTypeArgument = genericType.getActualTypeArguments()[index];
        if (actualTypeArgument instanceof Class) {
            Class<?> typeParamClass = (Class<?>) actualTypeArgument;
            return getTypeFromJavaType(reflectedField, typeParamClass);
        } else if (actualTypeArgument instanceof WildcardType) {
            return getUnknownType();
        } else {
            throw new UnsupportedOperationException(type.getSimpleName() +
                    " not supported in field " + reflectedField.getName());
        }
    }

    private CodeGenSpec.Type getUnknownType() {
        CodeGenSpec.Type type = new CodeGenSpec.Type();
        type.nativeType = CodeGenSpec.DataType.UNKNOWN;
        return type;
    }
}
