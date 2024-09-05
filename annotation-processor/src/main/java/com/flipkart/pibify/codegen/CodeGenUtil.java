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

    /**
     * Returns the entire generic declaration string for this field
     *
     * @param fieldSpec
     * @return
     */
    public static String getGenericTypeStringForField(CodeGenSpec.FieldSpec fieldSpec) {

        if (!isCollectionOrMap(fieldSpec.getType().getNativeType())) {
            throw new UnsupportedOperationException("Generic type string requested for non-containers");
        }
        return getGenericTypeStringForField(fieldSpec.getType());
    }

    /**
     * Returns the entire generic declaration string for this type
     * @param type
     * @return
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
            result.append(type.getNativeType().getAutoboxedClass().getCanonicalName());
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
            return type.getNativeType().getAutoboxedClass().getCanonicalName();
            //throw new UnsupportedOperationException();
        }
    }
}
