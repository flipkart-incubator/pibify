package com.flipkart.pibify;

import com.flipkart.pibify.serde.IDeserializer;
import com.flipkart.pibify.serde.PibifyDeserializer;
import com.google.protobuf.WireFormat;

import java.io.IOException;

/**
 * This class represents what a generated deserializer will look like
 * Author bageshwar.pn
 * Date 27/07/24
 */
public class ClassWithNativeFieldsDeserializer {

    public static ClassWithNativeFields deserialize(byte[] bytes) throws IOException {
        ClassWithNativeFields object = new ClassWithNativeFields();
        object.resetAll();

        IDeserializer deserializer = new PibifyDeserializer(bytes);
        int tag = deserializer.getNextTag();

        while (tag != 0) {
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
                    int i = deserializer.readByte();
                    object.setaByte((byte) i);
                    break;
                case 72:
                    object.setaShort((short) deserializer.readShort());
                    break;

            }
            tag = deserializer.getNextTag();
            tagMeta(tag);
        }

        return object;
    }

    private static void tagMeta(int tag) {
        System.out.println(WireFormat.getTagFieldNumber(tag) + ":" + WireType.values()[WireFormat.getTagWireType(tag)]);
    }

    enum WireType {
        WIRETYPE_VARINT(0),
        WIRETYPE_FIXED64(1),
        WIRETYPE_LENGTH_DELIMITED(2),
        WIRETYPE_START_GROUP(3),
        WIRETYPE_END_GROUP(4),
        WIRETYPE_FIXED32(5);

        final int wireType;

        WireType(int wireType) {
            this.wireType = wireType;
        }
    }
}
