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

package com.flipkart.pibify.codegen.stub;

import com.flipkart.pibify.codegen.PibifyCodeExecException;
import com.flipkart.pibify.serde.IDeserializer;
import com.flipkart.pibify.serde.ISerializer;

/**
 * This class is used for serde of SerializationContext.
 * Author bageshwar.pn
 * Date 13/11/24
 */
public final class SerializationContextHandler extends PibifyGenerated<SerializationContext> {

    @Override
    public void serialize(SerializationContext object, ISerializer serializer,
                          SerializationContext context) throws PibifyCodeExecException {
        if (object == null) {
            return;
        }
        try {
            serializer.writeShort(1, object.getVersion());
            for (String word : object.getDictionary()) {
                serializer.writeString(2, word);
            }

        } catch (Exception e) {
            throw new PibifyCodeExecException(e);
        }
    }

    @Override
    public SerializationContext deserialize(IDeserializer deserializer,
                                            Class<SerializationContext> clazz, SerializationContext context) throws
            PibifyCodeExecException {
        try {
            SerializationContext object = new SerializationContext();
            int tag = deserializer.getNextTag();
            while (tag != 0 && tag != PibifyGenerated.getEndObjectTag()) {
                switch (tag) {
                    case 8:
                        object.setVersion((short) deserializer.readShort());
                        break;
                    case 18:
                        object.addStringToDictionary(deserializer.readString());
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
}
