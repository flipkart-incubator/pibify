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

import java.io.IOException;
import java.util.Date;

/**
 * This class is used for serde of Date
 * Author bageshwar.pn
 * Date 15/01/25
 */
public class DateHandler extends PibifyGenerated<Date> {

    /**
     * Serializes a Date object to a long representation of milliseconds since epoch.
     *
     * @param object The Date object to be serialized, can be null
     * @param serializer The serializer used to write the Date's time value
     * @param context The serialization context (unused in this implementation)
     * @throws PibifyCodeExecException if serialization encounters an unexpected error
     * @throws RuntimeException if an IOException occurs during serialization
     */
    @Override
    public void serialize(Date object, ISerializer serializer, SerializationContext context) throws PibifyCodeExecException {
        if (object != null) {
            try {
                serializer.writeLong(1, object.getTime());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Deserializes a Date object from the provided deserializer.
     *
     * @param deserializer The deserializer used to read the Date object
     * @param type The target Date class type
     * @param context The serialization context for deserialization
     * @return A Date object reconstructed from the serialized data, or null if no valid date was found
     * @throws PibifyCodeExecException If an I/O error occurs during deserialization
     */
    @Override
    public Date deserialize(IDeserializer deserializer, Class<Date> type, SerializationContext context) throws PibifyCodeExecException {
        try {
            int tag = deserializer.getNextTag();
            Date date = null;
            while (tag != 0 && tag != PibifyGenerated.getEndObjectTag()) {
                switch (tag) {
                    case 8:
                        date = new Date(deserializer.readLong());
                        break;
                    default:
                        break;
                }
                tag = deserializer.getNextTag();
            }
            return date;
        } catch (IOException e) {
            throw new PibifyCodeExecException(e);
        }
    }
}
