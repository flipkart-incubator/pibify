package com.flipkart.pibify.codegen;

import com.flipkart.pibify.core.Pibify;
import com.squareup.javapoet.TypeName;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

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
        return spec.equals(TypeName.OBJECT);
    }

    public static boolean isNotNative(CodeGenSpec.DataType dataType) {
        return dataType == CodeGenSpec.DataType.OBJECT
                || dataType == CodeGenSpec.DataType.COLLECTION
                || dataType == CodeGenSpec.DataType.MAP
                || dataType == CodeGenSpec.DataType.ARRAY;
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
            } else if (type.getNativeType() == CodeGenSpec.DataType.ARRAY) {
                result.append(type.getContainerTypes().get(0).getGenericTypeSignature())
                        .append("[]");
            } else if (type.getNativeType() == CodeGenSpec.DataType.BYTE_ARRAY) {
                result.append(type.getGenericTypeSignature());
            } else {
                result.append(type.getNativeType().getAutoboxedClass().getCanonicalName());
            }

        }

        return result.toString();
    }

    private static String getClassNameFromType(CodeGenSpec.Type type) {
        String result;
        if (type.getNativeType() == CodeGenSpec.DataType.COLLECTION) {
            if (type.getReferenceType() != null) {
                result = type.getReferenceType().getPackageName() + "." + type.getReferenceType().getClassName() + "<";
            } else {
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


    /**
     * Recursively resolves all type parameters for the target class.
     */
    public static List<Type> resolveTargetTypes(Type type, Class<?> targetClass) {
        if (type instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType) type;
            Type rawType = paramType.getRawType();

            // If we found our target class, return all its type arguments
            if (rawType.equals(targetClass)) {
                return Arrays.stream(paramType.getActualTypeArguments())
                        .filter(t -> !(t instanceof TypeVariable)) /*Type variables are unresolvable*/
                        .collect(Collectors.toList());
            }

            // Search in nested types
            for (Type typeArg : paramType.getActualTypeArguments()) {
                List<Type> resolved = resolveTargetTypes(typeArg, targetClass);
                if (!resolved.isEmpty()) {
                    return resolved;
                }
            }
        }
        return Collections.emptyList();
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

    public static Type resolveTypeViaSuperClassHierarchy(Type actualTypeArgument, ParameterizedType subGenericType, ParameterizedType fieldGenericType, int index) {
        // This method tries to find the concrete generic type parameter based on the scenario
        // where subclass passes a subset of generic params to superclass as concrete params
        long typeVariablesInSuperClass = Arrays.stream(fieldGenericType.getActualTypeArguments()).filter(g -> g instanceof TypeVariable).count();
        long typeVariablesInSubClass = Arrays.stream(subGenericType.getActualTypeArguments()).filter(g -> g instanceof TypeVariable).count();

        if (typeVariablesInSuperClass == 1 && typeVariablesInSubClass == 0) {
            return subGenericType.getActualTypeArguments()[0];
        }
        if (typeVariablesInSuperClass == (subGenericType.getActualTypeArguments().length - typeVariablesInSubClass)) {
            // all type variables in super class have been provided as concrete types  in subclass
            if (subGenericType.getActualTypeArguments().length >= index) {
                return subGenericType.getActualTypeArguments()[index];
            }

        }

        return actualTypeArgument;
    }

    private static boolean containsPibifyAnnotation(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields()).anyMatch(field -> field.getAnnotation(Pibify.class) != null);
    }


    public static boolean isNonPibifyClass(Class<?> clazz) {
        if (clazz.isEnum()) {
            return true;
        } else {
            if (containsPibifyAnnotation(clazz)) {
                return false;
            } else {
                // check for superclass
                if (Object.class.equals(clazz) || clazz.isInterface()) {
                    return true;
                } else {
                    return isNonPibifyClass(clazz.getSuperclass());
                }
            }
        }
    }

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
