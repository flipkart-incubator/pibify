package com.flipkart.pibify.serde;

import com.flipkart.pibify.codegen.PibifyCodeExecException;
import com.flipkart.pibify.codegen.stub.PibifyGenerated;
import com.flipkart.pibify.codegen.stub.SerializationContext;

import java.io.IOException;

/**
 * Interface for a serializer
 * Author bageshwar.pn
 * Date 27/07/24
 */
public interface ISerializer {
    byte[] serialize() throws IOException;

    void writeBool(int index, boolean value) throws IOException;

    void writeBool(int index, Boolean value) throws IOException;

    void writeShort(int index, short value) throws IOException;

    void writeShort(int index, Short value) throws IOException;

    void writeByte(int index, byte value) throws IOException;

    void writeByte(int index, Byte value) throws IOException;

    void writeChar(int index, char value) throws IOException;

    void writeChar(int index, Character value) throws IOException;

    void writeInt(int index, int value) throws IOException;

    void writeInt(int index, Integer value) throws IOException;

    void writeLong(int index, long value) throws IOException;

    void writeLong(int index, Long value) throws IOException;

    void writeFloat(int index, float value) throws IOException;

    void writeFloat(int index, Float value) throws IOException;

    void writeDouble(int index, double value) throws IOException;

    void writeDouble(int index, Double value) throws IOException;

    void writeString(int index, String value) throws IOException;

    void writeEnum(int index, Enum<?> value) throws IOException;

    void writeObjectAsBytes(int index, byte[] value) throws IOException;

    void writeObject(int index, PibifyGenerated handler, Object object, SerializationContext context) throws PibifyCodeExecException, IOException;
}
