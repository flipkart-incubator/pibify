package com.flipkart.pibify;

import com.flipkart.pibify.serde.ISerializer;
import com.flipkart.pibify.serde.PibifySerializer;

import java.io.IOException;

/**
 * This class represents what a generated serializer will look like
 * Author bageshwar.pn
 * Date 27/07/24
 */
public class ClassWithNativeFieldsSerializer {
    // TODO This should be estimated based on the fields present in this class, estimation would break with collections
    private final int estimatedBufferSize = 0;

    public static byte[] serialize(ClassWithNativeFields pojo) throws IOException {
        ISerializer serializer = new PibifySerializer();
        serializer.writeString(1, pojo.getaString());
        serializer.writeInt(2, pojo.getAnInt());
        serializer.writeLong(3, pojo.getaLong());
        serializer.writeFloat(4, pojo.getaFloat());
        serializer.writeDouble(5, pojo.getaDouble());
        serializer.writeBool(6, pojo.isaBoolean());
        serializer.writeChar(7, pojo.getaChar());
        serializer.writeByte(8, pojo.getaByte());
        serializer.writeShort(9, pojo.getaShort());
        byte[] serializedBytes = serializer.serialize();

        /*
            for arrays
         * for (int i = 0; i < additionalItems_.size(); i++) {
         *       output.writeMessage(1, additionalItems_.get(i));
         *     }
         */
        return serializedBytes;
    }
}
