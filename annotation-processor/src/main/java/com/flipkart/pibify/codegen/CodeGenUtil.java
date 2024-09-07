package com.flipkart.pibify.codegen;

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
            if (type.getNativeType() == CodeGenSpec.DataType.OBJECT) {
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
}
