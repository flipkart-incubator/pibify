package com.flipkart.pibify.codegen;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * This class is used for holding common util methods
 * Author bageshwar.pn
 * Date 24/08/24
 */
public class CodeGenUtil {

    public static boolean isCollectionOrMap(CodeGenSpec.DataType type) {
        return (type == CodeGenSpec.DataType.COLLECTION || type == CodeGenSpec.DataType.MAP);
    }

    public static boolean isReferenceOfInnerClass(CodeGenSpec.FieldSpec fieldSpec) {
        return fieldSpec.getType().getNativeType() == CodeGenSpec.DataType.OBJECT
                && fieldSpec.getType().getReferenceType().isInnerClass();
    }

    public static boolean isArray(CodeGenSpec.FieldSpec fieldSpec) {
        return isArray(fieldSpec.getType().getNativeType());
    }

    public static boolean isArray(CodeGenSpec.DataType type) {
        return type == CodeGenSpec.DataType.ARRAY;
    }

    public static boolean isCollection(CodeGenSpec.FieldSpec fieldSpec) {
        return isCollection(fieldSpec.getType().getNativeType());
    }

    public static boolean isCollection(CodeGenSpec.DataType type) {
        return type == CodeGenSpec.DataType.COLLECTION;
    }

    public static boolean isObject(CodeGenSpec.FieldSpec fieldSpec) {
        return isObject(fieldSpec.getType().getNativeType());
    }

    public static boolean isObject(CodeGenSpec.DataType type) {
        return type == CodeGenSpec.DataType.OBJECT;
    }

    public static boolean isJavaLangObject(CodeGenSpec spec) {
        return (spec.getClassName().equals("Object") && spec.getPackageName().equals("java.lang"));
    }

    public static boolean isJavaLangObject(TypeName spec) {
        return spec.equals(ClassName.get(Object.class));
    }

    public static boolean isNotNative(CodeGenSpec.DataType dataType) {
        return dataType == CodeGenSpec.DataType.OBJECT
                || dataType == CodeGenSpec.DataType.COLLECTION
                || dataType == CodeGenSpec.DataType.MAP;
    }

    /**
     * Returns the entire generic declaration string for this field
     *
     * @param fieldSpec fieldspec
     * @return string signature
     */
    public static String getGenericTypeStringForField(CodeGenSpec.FieldSpec fieldSpec) {
        return getGenericTypeStringForField(fieldSpec.getType());
    }

    /**
     * Returns the entire generic declaration string for this type
     * @param type type
     * @return string signature
     */
    public static String getGenericTypeStringForField(CodeGenSpec.Type type) {
        StringBuilder result = new StringBuilder();

        if (isCollectionOrMap(type.getNativeType())) {
            Deque<CodeGenSpec.Type> deque = new ArrayDeque<>();
            deque.add(type);

            while (!deque.isEmpty()) {
                CodeGenSpec.Type first = deque.removeFirst();
                result.append(getClassNameFromType(first));
            }
        } else {
            if (type.getNativeType() == CodeGenSpec.DataType.OBJECT
                    || type.getNativeType() == CodeGenSpec.DataType.ENUM) {
                result.append(type.getReferenceType().getPackageName())
                        .append(".")
                        .append(type.getReferenceType().getClassName());
            } else {
                result.append(type.getNativeType().getAutoboxedClass().getCanonicalName());
            }

        }

        return result.toString();
    }

    private static String getClassNameFromType(CodeGenSpec.Type type) {
        String result;
        if (type.getNativeType() == CodeGenSpec.DataType.COLLECTION) {
            switch (type.getCollectionType()) {
                case SET:
                    result = "java.util.Set<";
                    break;
                case DEQUE:
                    result = "java.util.Dequeue<";
                    break;
                case QUEUE:
                    result = "java.util.Stack<";
                    break;
                case LIST:
                default:
                    result = "java.util.List<";
            }
            result += getGenericTypeStringForField(type.getContainerTypes().get(0));
            return result + ">";
        } else if (type.getNativeType() == CodeGenSpec.DataType.MAP) {
            result = "java.util.Map<";
            result += getGenericTypeStringForField(type.getContainerTypes().get(0));
            result += ",";
            result += getGenericTypeStringForField(type.getContainerTypes().get(1));
            return result + ">";
        } else {
            throw new UnsupportedOperationException();
        }
    }

    // Credits to https://stackoverflow.com/a/19363555
    public static Class<?> determineType(Type fieldGenericType, Class<?> declaringClass, Class<?> underProcessingType) {
        Type resolvedType = getType(underProcessingType, fieldGenericType, declaringClass).type;
        if (resolvedType instanceof Class) {
            return (Class<?>) resolvedType;
        } else {
            return Object.class;
        }
    }

    /*static TypeInfo getType(Class<?> clazz, Field field) {
        //TypeVariable<?> genericTyp = (TypeVariable<?>) field.getGenericType();
        return getType(clazz, field, field.getGenericType());
    }*/

    public static TypeInfo getType(Class<?> clazz, Type genericType, Class<?> fieldDeclaringClass) {
        TypeInfo type = new TypeInfo(null, null);

        if (genericType instanceof TypeVariable<?> || genericType instanceof ParameterizedType) {
            Class<?> superClazz = clazz.getSuperclass();

            if (clazz.getGenericSuperclass() instanceof ParameterizedType) {
                ParameterizedType paramType = (ParameterizedType) clazz.getGenericSuperclass();
                TypeVariable<?>[] superTypeParameters = superClazz.getTypeParameters();
                if (!Object.class.equals(paramType)) {
                    if (fieldDeclaringClass.equals(superClazz)) {
                        // this is the root class an starting point for this search
                        type.name = genericType;
                        type.type = null;
                    } else {
                        type = getType(superClazz, genericType, fieldDeclaringClass);
                    }
                }
                if (type.type == null || type.type instanceof TypeVariable<?>) {
                    // lookup if type is not found or type needs a lookup in current concrete class
                    for (int j = 0; j < superClazz.getTypeParameters().length; ++j) {
                        TypeVariable<?> superTypeParam = superTypeParameters[j];
                        if (superTypeParam.equals(type.name)) {
                            type.type = paramType.getActualTypeArguments()[j];
                            Type[] typeParameters = clazz.getTypeParameters();
                            for (Type typeParam : typeParameters) {
                                TypeVariable<?> objectOfComparison = superTypeParam;
                                if (type.type instanceof TypeVariable<?>) {
                                    objectOfComparison = (TypeVariable<?>) type.type;
                                }
                                if (objectOfComparison.getName().equals(((TypeVariable<?>) typeParam).getName())) {
                                    type.name = typeParam;
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }
            }
        } else {
            type.type = genericType;
        }

        return type;
    }

    protected static class TypeInfo {
        Type type;
        Type name;

        public TypeInfo(Type type, Type name) {
            this.type = type;
            this.name = name;
        }

    }
}
