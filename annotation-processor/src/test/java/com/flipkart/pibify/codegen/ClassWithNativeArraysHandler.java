package com.flipkart.pibify.codegen;

import com.flipkart.pibify.codegen.stub.PibifyGenerated;
import com.flipkart.pibify.serde.IDeserializer;
import com.flipkart.pibify.serde.ISerializer;
import com.flipkart.pibify.serde.PibifyDeserializer;
import com.flipkart.pibify.serde.PibifySerializer;
import com.flipkart.pibify.test.data.ClassWithNativeArrays;

import java.util.Arrays;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 10/08/24
 */
public final class ClassWithNativeArraysHandler extends PibifyGenerated<ClassWithNativeArrays> {
    @Override
    public byte[] serialize(ClassWithNativeArrays object) throws PibifyCodeExecException {
        ISerializer serializer = new PibifySerializer();
        try {
            for (int i = 0; i < object.getaString().length; i++) {
                serializer.writeString(1, object.getaString()[i]);
            }
            for (int i = 0; i < object.getAnInt().length; i++) {
                serializer.writeInt(2, object.getAnInt()[i]);
            }
            for (int i = 0; i < object.getaBoolean().length; i++) {
                serializer.writeBool(3, object.getaBoolean()[i]);
            }
            return serializer.serialize();
        } catch (Exception e) {
            throw new PibifyCodeExecException(e);
        }
    }

    @Override
    public ClassWithNativeArrays deserialize(byte[] bytes) throws PibifyCodeExecException {
        try {
            ClassWithNativeArrays object = new ClassWithNativeArrays();
            IDeserializer deserializer = new PibifyDeserializer(bytes);
            int tag = deserializer.getNextTag();
            while (tag != 0) {
                switch (tag) {
                    case 10:
                        String[] newArray10;
                        String[] oldArray10 = object.getaString();
                        String val10 = deserializer.readString();
                        if (oldArray10 == null) {
                            newArray10 = new String[]{val10};
                        } else {
                            newArray10 = Arrays.copyOf(oldArray10, oldArray10.length + 1);
                            newArray10[oldArray10.length] = val10;
                        }
                        object.setaString(newArray10);
                        break;
                    case 16:
                        int[] newArray16;
                        int[] oldArray16 = object.getAnInt();
                        int val16 = deserializer.readInt();
                        if (oldArray16 == null) {
                            newArray16 = new int[]{val16};
                        } else {
                            newArray16 = Arrays.copyOf(oldArray16, oldArray16.length + 1);
                            newArray16[oldArray16.length] = val16;
                        }
                        object.setAnInt(newArray16);
                        break;
                    case 24:
                        boolean[] newArray24;
                        boolean[] oldArray24 = object.getaBoolean();
                        boolean val24 = deserializer.readBool();
                        if (oldArray24 == null) {
                            newArray24 = new boolean[]{val24};
                        } else {
                            newArray24 = Arrays.copyOf(oldArray24, oldArray24.length + 1);
                            newArray24[oldArray24.length] = val24;
                        }
                        object.setaBoolean(newArray24);
                        break;
                    default:
                        throw new UnsupportedOperationException("Unable to find tag in gen code");
                }
                tag = deserializer.getNextTag();
            }
            return object;
        } catch (Exception e) {
            throw new PibifyCodeExecException(e);
        }
    }
}