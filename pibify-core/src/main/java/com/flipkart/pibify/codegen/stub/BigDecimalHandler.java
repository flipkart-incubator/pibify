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
