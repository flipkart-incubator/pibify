package com.flipkart.pibify.serde;

import com.google.protobuf.CodedOutputStream;

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

    public PibifySerializer() {
        // TODO use pre-computed estimates on the size of the buffer
        outputStream = new ByteArrayOutputStream(256);
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
    public void writeShort(int index, short value) throws IOException {
        codedOutputStream.writeInt32(index, value);
    }

    @Override
    public void writeByte(int index, byte value) throws IOException {
        codedOutputStream.writeInt32(index, value);
    }

    @Override
    public void writeChar(int index, char value) throws IOException {
        codedOutputStream.writeInt32(index, value);
    }

    @Override
    public void writeInt(int index, int value) throws IOException {
        codedOutputStream.writeInt32(index, value);
    }

    @Override
    public void writeLong(int index, long value) throws IOException {
        codedOutputStream.writeInt64(index, value);
    }

    @Override
    public void writeFloat(int index, float value) throws IOException {
        codedOutputStream.writeFloat(index, value);
    }

    @Override
    public void writeDouble(int index, double value) throws IOException {
        codedOutputStream.writeDouble(index, value);
    }

    @Override
    public void writeString(int index, String value) throws IOException {
        codedOutputStream.writeString(index, value);
    }

    @Override
    public void writeObjectAsBytes(int index, byte[] value) throws IOException {
        codedOutputStream.writeByteArray(index, value);
    }
}
