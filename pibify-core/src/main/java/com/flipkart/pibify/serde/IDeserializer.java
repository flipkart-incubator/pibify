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

import java.io.IOException;

/**
 * Interface for a deserializer
 * Author bageshwar.pn
 * Date 27/07/24
 */
public interface IDeserializer {
    Object deserialize(byte[] bytes) throws IOException;

    int getNextTag() throws IOException;

    boolean readBool() throws IOException;

    int readShort() throws IOException;

    int readByte() throws IOException;

    int readChar() throws IOException;

    int readInt() throws IOException;

    long readLong() throws IOException;

    float readFloat() throws IOException;

    double readDouble() throws IOException;

    String readString() throws IOException;

    byte[] readObjectAsBytes() throws IOException;

    int readEnum() throws IOException;
}
