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

package com.flipkart.pibify.serde;

import com.flipkart.pibify.codegen.PibifyCodeExecException;
import com.flipkart.pibify.codegen.stub.PibifyGenerated;
import com.flipkart.pibify.codegen.stub.SerializationContext;

import java.io.IOException;

/**
 * Interface for a serializer
 * Author bageshwar.pn
 * Date 27/07/24
 */
public interface ISerializer {
    byte[] serialize() throws IOException;

    void writeBool(int index, boolean value) throws IOException;

    void writeBool(int index, Boolean value) throws IOException;

    void writeShort(int index, short value) throws IOException;

    void writeShort(int index, Short value) throws IOException;

    void writeByte(int index, byte value) throws IOException;

    void writeByte(int index, Byte value) throws IOException;

    void writeChar(int index, char value) throws IOException;

    void writeChar(int index, Character value) throws IOException;

    void writeInt(int index, int value) throws IOException;

    void writeInt(int index, Integer value) throws IOException;

    void writeLong(int index, long value) throws IOException;

    void writeLong(int index, Long value) throws IOException;

    void writeFloat(int index, float value) throws IOException;

    void writeFloat(int index, Float value) throws IOException;

    void writeDouble(int index, double value) throws IOException;

    void writeDouble(int index, Double value) throws IOException;

    void writeString(int index, String value) throws IOException;

    void writeEnum(int index, Enum<?> value) throws IOException;

    void writeObjectAsBytes(int index, byte[] value) throws IOException;

    void writeObject(int index, PibifyGenerated handler, Object object, SerializationContext context) throws PibifyCodeExecException, IOException;
}
