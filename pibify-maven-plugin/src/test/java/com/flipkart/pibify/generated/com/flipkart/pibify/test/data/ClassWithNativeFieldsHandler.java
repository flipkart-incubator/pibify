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

package com.flipkart.pibify.generated.com.flipkart.pibify.test.data;

import com.flipkart.pibify.codegen.PibifyCodeExecException;
import com.flipkart.pibify.codegen.stub.AbstractPibifyHandlerCache;
import com.flipkart.pibify.codegen.stub.PibifyGenerated;
import com.flipkart.pibify.codegen.stub.SerializationContext;
import com.flipkart.pibify.serde.IDeserializer;
import com.flipkart.pibify.serde.ISerializer;
import com.flipkart.pibify.test.data.ClassWithNativeFields;

import java.util.HashMap;
import java.util.Map;

public final class ClassWithNativeFieldsHandler extends PibifyGenerated<ClassWithNativeFields> {
    private static final Map<String, PibifyGenerated> HANDLER_MAP;

    static {
        HANDLER_MAP = new HashMap<>();
    }

    @Override
    public void serialize(ClassWithNativeFields object, ISerializer serializer,
                          SerializationContext context) throws PibifyCodeExecException {
        if (object == null) {
            return;
        }
        try {
            serializer.writeString(1, object.getaString());
            serializer.writeInt(2, object.getAnInt());
            serializer.writeLong(3, object.getaLong());
            serializer.writeFloat(4, object.getaFloat());
            serializer.writeDouble(5, object.getaDouble());
            serializer.writeBool(6, object.isaBoolean());
            serializer.writeChar(7, object.getaChar());
            serializer.writeByte(8, object.getaByte());
            serializer.writeShort(9, object.getaShort());
            serializer.writeInt(10, context.addStringToDictionary(object.getdString1()));
            serializer.writeInt(11, context.addStringToDictionary(object.getdString2()));
            serializer.writeInt(12, context.addStringToDictionary(object.dString3));
        } catch (Exception e) {
            throw new PibifyCodeExecException(e);
        }
    }

    @Override
    public ClassWithNativeFields deserialize(IDeserializer deserializer,
                                             Class<ClassWithNativeFields> clazz, SerializationContext context) throws
            PibifyCodeExecException {
        try {
            ClassWithNativeFields object = new ClassWithNativeFields();
            int tag = deserializer.getNextTag();
            while (tag != 0 && tag != PibifyGenerated.getEndObjectTag()) {
                switch (tag) {
                    case 10:
                        object.setaString(deserializer.readString());
                        break;
                    case 16:
                        object.setAnInt(deserializer.readInt());
                        break;
                    case 24:
                        object.setaLong(deserializer.readLong());
                        break;
                    case 37:
                        object.setaFloat(deserializer.readFloat());
                        break;
                    case 41:
                        object.setaDouble(deserializer.readDouble());
                        break;
                    case 48:
                        object.setaBoolean(deserializer.readBool());
                        break;
                    case 56:
                        object.setaChar((char) deserializer.readChar());
                        break;
                    case 64:
                        object.setaByte((byte) deserializer.readByte());
                        break;
                    case 72:
                        object.setaShort((short) deserializer.readShort());
                        break;
                    case 80:
                        object.setdString1(context.getWord(deserializer.readInt()));
                        break;
                    case 88:
                        object.setdString2(context.getWord(deserializer.readInt()));
                        break;
                    case 96:
                        object.dString3 = (context.getWord(deserializer.readInt()));
                        break;
                    default:
                        break;
                }
                tag = deserializer.getNextTag();
            }
            return object;
        } catch (Exception e) {
            throw new PibifyCodeExecException(e);
        }
    }

    @Override
    public void initialize(AbstractPibifyHandlerCache pibifyHandlerCache) {
        for (PibifyGenerated internalHandler : HANDLER_MAP.values()) {
            internalHandler.initialize(pibifyHandlerCache);
        }
    }
}