package com.flipkart.pibify.codegen;

/**
 * This class is used for holding common util methods
 * Author bageshwar.pn
 * Date 24/08/24
 */
public class CodeGenUtil {

    public static boolean isCollectionOrMap(CodeGenSpec.DataType type) {
        return (type == CodeGenSpec.DataType.COLLECTION || type == CodeGenSpec.DataType.MAP);
    }

    public static String getGenericTypeStringForField(CodeGenSpec.FieldSpec fieldSpec) {

        if (!isCollectionOrMap(fieldSpec.getType().nativeType)) {
            throw new UnsupportedOperationException("Generic type string requested for non-containers");
        }
        return getGenericTypeStringForField(fieldSpec.getType());
    }

    public static String getGenericTypeStringForField(CodeGenSpec.Type type) {
        StringBuilder result = new StringBuilder();
        int closing = 0;
        if (isCollectionOrMap(type.nativeType)) {
            while (type.containerTypes != null) {
                result.append(getClassNameFromType(type)).append("<");
                closing++;
                // the below line is failing for maps
                type = type.containerTypes.get(0);
            }
            result.append(type.nativeType.getAutoboxedClass().getCanonicalName());
        } else {
            result.append(type.nativeType.getAutoboxedClass().getCanonicalName());
        }

        while (closing > 0) {
            result.append(">");
            closing--;
        }

        return result.toString();
    }

    public static String getClassNameFromType(CodeGenSpec.Type type) {
        if (type.nativeType == CodeGenSpec.DataType.COLLECTION) {
            switch (type.collectionType) {
                case SET:
                    return "java.util.Set";
                case DEQUE:
                    return "java.util.Dequeue";
                case QUEUE:
                    return "java.util.Stack";
                case LIST:
                default:
                    return "java.util.List";
            }
        } else if (type.nativeType == CodeGenSpec.DataType.MAP) {
            String result = "java.util.Map<";
            result += getGenericTypeStringForField(type.containerTypes.get(0));
            result += ",";
            result += getGenericTypeStringForField(type.containerTypes.get(1));
            return result + ">";
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
