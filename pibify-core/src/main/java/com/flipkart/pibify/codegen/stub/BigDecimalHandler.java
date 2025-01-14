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
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * This class is used for serde of BigDecimal
 * Author bageshwar.pn
 * Date 13/11/24
 */
public class BigDecimalHandler extends PibifyGenerated<BigDecimal> {

    @Override
    public void serialize(BigDecimal object, ISerializer serializer, SerializationContext context) throws PibifyCodeExecException {
        if (object != null) {
            try {
                serializer.writeObjectAsBytes(1, object.unscaledValue().toByteArray());
                serializer.writeInt(2, object.scale());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public BigDecimal deserialize(IDeserializer deserializer, Class<BigDecimal> type, SerializationContext context) throws PibifyCodeExecException {
        try {
            BigDecimal bigDecimal;
            int tag = deserializer.getNextTag();
            byte[] unscaledValue = new byte[0];
            int scale = 0;
            while (tag != 0 && tag != PibifyGenerated.getEndObjectTag()) {
                switch (tag) {
                    case 10:
                        unscaledValue = deserializer.readObjectAsBytes();
                        break;
                    case 16:
                        scale = deserializer.readInt();
                    default:
                        break;
                }
                tag = deserializer.getNextTag();
            }
            bigDecimal = new BigDecimal(new BigInteger(unscaledValue), scale);
            return bigDecimal;
        } catch (IOException e) {
            throw new PibifyCodeExecException(e);
        }
    }
}
