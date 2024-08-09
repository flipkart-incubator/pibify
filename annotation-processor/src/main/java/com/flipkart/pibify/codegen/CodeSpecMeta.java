package com.flipkart.pibify.codegen;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to store all meta required for generating codespec
 * Author bageshwar.pn
 * Date 09/08/24
 */
public final class CodeSpecMeta {

    public static final Map<String, CodeGenSpec.DataType> NATIVE_TYPE_MAP = getNativeTypeMap();

    private static Map<String, CodeGenSpec.DataType> getNativeTypeMap() {
        Map<String, CodeGenSpec.DataType> map = new HashMap<>();
        map.put("String", CodeGenSpec.DataType.STRING);
        map.put("int", CodeGenSpec.DataType.INT);
        map.put("long", CodeGenSpec.DataType.LONG);
        map.put("float", CodeGenSpec.DataType.FLOAT);
        map.put("double", CodeGenSpec.DataType.DOUBLE);
        map.put("short", CodeGenSpec.DataType.SHORT);
        map.put("byte", CodeGenSpec.DataType.BYTE);
        map.put("boolean", CodeGenSpec.DataType.BOOLEAN);
        map.put("char", CodeGenSpec.DataType.CHAR);

        return ImmutableMap.copyOf(map);
    }
}
