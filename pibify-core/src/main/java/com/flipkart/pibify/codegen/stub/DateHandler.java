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
