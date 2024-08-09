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
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class generates the CodeGenSpec based on incoming Class via Reflection
 * Author bageshwar.pn
 * Date 09/08/24
 */
public class BeanIntrospectorBasedCodeGenSpecCreator implements ICodeGenSpecCreator {

    @Override
    public CodeGenSpec create(Class<?> type) throws CodeGenException {

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(type);

            Map<String, PropertyDescriptor> namesToBeanInfo = Arrays.stream(beanInfo.getPropertyDescriptors())
                    .collect(Collectors.toMap(FeatureDescriptor::getName, f -> f));

            CodeGenSpec spec = new CodeGenSpec(type.getCanonicalName(), type.getSimpleName());

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

    private CodeGenSpec.Type getTypeFromJavaType(Field reflectedField, Class<?> type) {
        CodeGenSpec.Type specType = new CodeGenSpec.Type();
        CodeGenSpec.DataType nativeType = CodeSpecMeta.CLASS_TO_TYPE_MAP.get(type);

        if (nativeType != null) {
            specType.nativeType = nativeType;
        } else {

            if (type.isArray()) {
                Class<?> arrayType = type.getComponentType();
                specType.nativeType = CodeGenSpec.DataType.ARRAY;
                specType.containerType = getTypeFromJavaType(reflectedField, arrayType);
            } else if (Collection.class.isAssignableFrom(type)) {
                specType.nativeType = CodeGenSpec.DataType.COLLECTION;
                ParameterizedType genericType = (ParameterizedType) reflectedField.getGenericType();
                Type actualTypeArgument = genericType.getActualTypeArguments()[0];
                if (actualTypeArgument instanceof Class) {
                    Class<?> typeParamClass = (Class<?>) actualTypeArgument;
                    specType.containerType = getTypeFromJavaType(reflectedField, typeParamClass);
                } else if (actualTypeArgument instanceof WildcardType) {
                    specType.containerType = getUnknownType();
                } else {
                    throw new UnsupportedOperationException(type.getSimpleName() + " not supported");
                }
            } else {
                // not supported
                throw new UnsupportedOperationException(type.getSimpleName() + " not supported");
            }
        }

        return specType;
    }

    private CodeGenSpec.Type getUnknownType() {
        CodeGenSpec.Type type = new CodeGenSpec.Type();
        type.nativeType = CodeGenSpec.DataType.UNKNOWN;
        return type;
    }
}
