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

import com.google.protobuf.CodedInputStream;

import java.io.IOException;

/**
 * This class serves as the base deserializer that the generated code uses.
 * Author bageshwar.pn
 * Date 27/07/24
 */
public class PibifyDeserializer extends BaseSerde implements IDeserializer {

    private final CodedInputStream codedInputStream;

    public PibifyDeserializer(byte[] bytes) {
        codedInputStream = CodedInputStream.newInstance(bytes);
    }

    @Override
    public Object deserialize(byte[] bytes) {
        return null;
    }

    @Override
    public int getNextTag() throws IOException {
        return codedInputStream.readTag();
    }

    @Override
    public boolean readBool() throws IOException {
        return codedInputStream.readBool();
    }

    @Override
    public int readShort() throws IOException {
        return codedInputStream.readInt32();
    }

    @Override
    public int readByte() throws IOException {
        return codedInputStream.readInt32();
    }

    @Override
    public int readChar() throws IOException {
        return codedInputStream.readInt32();
    }

    @Override
    public int readInt() throws IOException {
        return codedInputStream.readInt32();
    }

    @Override
    public long readLong() throws IOException {
        return codedInputStream.readInt64();
    }

    @Override
    public float readFloat() throws IOException {
        return codedInputStream.readFloat();
    }

    @Override
    public double readDouble() throws IOException {
        return codedInputStream.readDouble();
    }

    @Override
    public String readString() throws IOException {
        return codedInputStream.readStringRequireUtf8();
    }

    @Override
    public byte[] readObjectAsBytes() throws IOException {
        return codedInputStream.readByteArray();
    }

    @Override
    public int readEnum() throws IOException {
        return codedInputStream.readInt32();
    }
}
