package com.flipkart.pibify.codegen;

import com.flipkart.pibify.core.Pibify;
import com.flipkart.pibify.validation.InvalidPibifyAnnotation;
import com.google.common.collect.ImmutableMap;
import com.google.protobuf.WireFormat;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used for generating the tag int based on field number and field type.
 * Author bageshwar.pn
 * Date 27/07/24
 */
public class TagPredictor {

    private static final Map<Class<?>, Integer> fieldTypeToWireTypeMap = ImmutableMap.copyOf(getFieldTypeToWireTypeMap());
    private static final int TAG_TYPE_BITS = 3;
    private static final int TAG_TYPE_MASK = (1 << TAG_TYPE_BITS) - 1;

    private static Map<Class<?>, Integer> getFieldTypeToWireTypeMap() {
        Map<Class<?>, Integer> map = new HashMap<>();
        map.put(int.class, WireFormat.WIRETYPE_VARINT);
        map.put(long.class, WireFormat.WIRETYPE_VARINT);
        map.put(float.class, WireFormat.WIRETYPE_FIXED32);
        map.put(double.class, WireFormat.WIRETYPE_FIXED64);
        map.put(short.class, WireFormat.WIRETYPE_VARINT);
        map.put(byte.class, WireFormat.WIRETYPE_VARINT);
        map.put(char.class, WireFormat.WIRETYPE_VARINT);
        map.put(boolean.class, WireFormat.WIRETYPE_VARINT);
        map.put(String.class, WireFormat.WIRETYPE_LENGTH_DELIMITED);
        map.put(Object.class, WireFormat.WIRETYPE_LENGTH_DELIMITED);
        map.put(byte[].class, WireFormat.WIRETYPE_LENGTH_DELIMITED);
        return map;
    }

    public static int getTagBasedOnField(Field field) throws InvalidPibifyAnnotation {
        Pibify annotation = field.getAnnotation(Pibify.class);
        if (annotation != null) {
            return makeTag(annotation.value(), fieldTypeToWireTypeMap.get(field.getType()));
        } else {
            throw new InvalidPibifyAnnotation(InvalidPibifyAnnotation.ErrorCodes.MISSING_INDEX);
        }
    }

    public static int getTagBasedOnField(CodeGenSpec.FieldSpec field) throws InvalidPibifyAnnotation {
        return makeTag(field.getIndex(), fieldTypeToWireTypeMap.get(field.getType().nativeType.getClazz()));
    }

    public static int getTagBasedOnField(int index, Class<?> clazz) throws InvalidPibifyAnnotation {
        return makeTag(index, fieldTypeToWireTypeMap.get(clazz));
    }


    static int makeTag(final int fieldNumber, final int wireType) {
        return (fieldNumber << TAG_TYPE_BITS) | wireType;
    }
}
