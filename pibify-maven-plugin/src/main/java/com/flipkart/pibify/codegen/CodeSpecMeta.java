/*
 *
 *  *Copyright [2025] [Original Author]
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.flipkart.pibify.codegen;

import com.pibify.shaded.com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to store all meta required for generating codespec
 * Author bageshwar.pn
 * Date 09/08/24
 */
public final class CodeSpecMeta {

    public static final Map<Class<?>, CodeGenSpec.DataType> CLASS_TO_TYPE_MAP = getClassToTypeMap();

    private static Map<Class<?>, CodeGenSpec.DataType> getClassToTypeMap() {
        Map<Class<?>, CodeGenSpec.DataType> map = new HashMap<>();
        map.put(Integer.class, CodeGenSpec.DataType.INT);
        map.put(Long.class, CodeGenSpec.DataType.LONG);
        map.put(Float.class, CodeGenSpec.DataType.FLOAT);
        map.put(Double.class, CodeGenSpec.DataType.DOUBLE);
        map.put(Short.class, CodeGenSpec.DataType.SHORT);
        map.put(Byte.class, CodeGenSpec.DataType.BYTE);
        map.put(Boolean.class, CodeGenSpec.DataType.BOOLEAN);
        map.put(Character.class, CodeGenSpec.DataType.CHAR);
        map.put(String.class, CodeGenSpec.DataType.STRING);
        map.put(Enum.class, CodeGenSpec.DataType.INT);

        map.put(int.class, CodeGenSpec.DataType.INT);
        map.put(long.class, CodeGenSpec.DataType.LONG);
        map.put(float.class, CodeGenSpec.DataType.FLOAT);
        map.put(double.class, CodeGenSpec.DataType.DOUBLE);
        map.put(short.class, CodeGenSpec.DataType.SHORT);
        map.put(byte.class, CodeGenSpec.DataType.BYTE);
        map.put(boolean.class, CodeGenSpec.DataType.BOOLEAN);
        map.put(char.class, CodeGenSpec.DataType.CHAR);

        return ImmutableMap.copyOf(map);
    }
}
