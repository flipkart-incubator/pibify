package com.flipkart.pibify.generated.com.flipkart.pibify.test;

import com.flipkart.pibify.codegen.PibifyCodeExecException;
import com.flipkart.pibify.codegen.stub.PibifyGenerated;
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
    public void serialize(ClassWithNativeFields object, ISerializer serializer) throws PibifyCodeExecException {
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
        } catch (Exception e) {
            throw new PibifyCodeExecException(e);
        }
    }

    @Override
    public ClassWithNativeFields deserialize(IDeserializer deserializer, Class<ClassWithNativeFields> clazz) throws
            PibifyCodeExecException {
        try {
            ClassWithNativeFields object = new ClassWithNativeFields();
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
                        object.setaByte((byte) deserializer.readByte());
                        break;
                    case 72:
                        object.setaShort((short) deserializer.readShort());
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