package com.flipkart.pibify.serde;

import com.flipkart.pibify.codegen.PibifyCodeExecException;
import com.flipkart.pibify.codegen.stub.PibifyGenerated;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.WireFormat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * This class serves as the base serializer that the generated code uses.
 * Author bageshwar.pn
 * Date 27/07/24
 */
public class PibifySerializer extends BaseSerde implements ISerializer {

    private final CodedOutputStream codedOutputStream;
    private final ByteArrayOutputStream outputStream;

    public PibifySerializer(int size) {
        // TODO use pre-computed estimates on the size of the buffer
        outputStream = new ByteArrayOutputStream(size);
        codedOutputStream = CodedOutputStream.newInstance(outputStream);
    }

    @Override
    public byte[] serialize() throws IOException {
        codedOutputStream.flush();
        return outputStream.toByteArray();
    }

    @Override
    public void writeBool(int index, boolean value) throws IOException {
        codedOutputStream.writeBool(index, value);
    }

    @Override
    public void writeBool(int index, Boolean value) throws IOException {
        if (value != null) {
            codedOutputStream.writeBool(index, value);
        }
    }

    @Override
    public void writeShort(int index, short value) throws IOException {
        codedOutputStream.writeInt32(index, value);
    }

    @Override
    public void writeShort(int index, Short value) throws IOException {
        if (value != null) {
            codedOutputStream.writeInt32(index, value);
        }
    }

    @Override
    public void writeByte(int index, byte value) throws IOException {
        codedOutputStream.writeInt32(index, value);
    }

    @Override
    public void writeByte(int index, Byte value) throws IOException {
        if (value != null) {
            codedOutputStream.writeInt32(index, value);
        }
    }

    @Override
    public void writeChar(int index, char value) throws IOException {
        codedOutputStream.writeInt32(index, value);
    }

    @Override
    public void writeChar(int index, Character value) throws IOException {
        if (value != null) {
            codedOutputStream.writeInt32(index, value);
        }
    }

    @Override
    public void writeInt(int index, int value) throws IOException {
        codedOutputStream.writeInt32(index, value);
    }

    @Override
    public void writeInt(int index, Integer value) throws IOException {
        if (value != null) {
            codedOutputStream.writeInt32(index, value);
        }
    }

    @Override
    public void writeLong(int index, long value) throws IOException {
        codedOutputStream.writeInt64(index, value);
    }

    @Override
    public void writeLong(int index, Long value) throws IOException {
        if (value != null) {
            codedOutputStream.writeInt64(index, value);
        }
    }

    @Override
    public void writeFloat(int index, float value) throws IOException {
        codedOutputStream.writeFloat(index, value);
    }

    @Override
    public void writeFloat(int index, Float value) throws IOException {
        if (value != null) {
            codedOutputStream.writeFloat(index, value);
        }
    }

    @Override
    public void writeDouble(int index, double value) throws IOException {
        codedOutputStream.writeDouble(index, value);
    }

    @Override
    public void writeDouble(int index, Double value) throws IOException {
        if (value != null) {
            codedOutputStream.writeDouble(index, value);
        }
    }

    @Override
    public void writeString(int index, String value) throws IOException {
        if (value != null) {
            codedOutputStream.writeString(index, value);
        }
    }

    @Override
    public void writeEnum(int index, Enum<?> value) throws IOException {
        if (value != null) {
            writeInt(index, value.ordinal());
        }
    }

    @Override
    public void writeObjectAsBytes(int index, byte[] value) throws IOException {
        if (value != null) {
            codedOutputStream.writeByteArray(index, value);
        }
    }

    @Override
    public void writeObject(int index, PibifyGenerated handler, Object value) throws PibifyCodeExecException, IOException {
        if (value != null) {
            codedOutputStream.writeTag(index, WireFormat.WIRETYPE_LENGTH_DELIMITED);
            ;
            handler.serialize(value, this);
            codedOutputStream.writeTag(1, WireFormat.WIRETYPE_END_GROUP);
        }
    }
}
