package com.flipkart.pibify.serde;

import com.google.protobuf.CodedInputStream;

import java.io.IOException;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 27/07/24
 */
public class PibifyDeserializer extends BaseSerde implements IDeserializer {

    private final CodedInputStream codedInputStream;

    public PibifyDeserializer(byte[] bytes) {
        codedInputStream = CodedInputStream.newInstance(bytes);
    }

    @Override
    public Object deserialize(byte[] bytes) {
        return null;
    }

    @Override
    public int getNextTag() throws IOException {
        return codedInputStream.readTag();
    }

    @Override
    public boolean readBool() throws IOException {
        return codedInputStream.readBool();
    }

    @Override
    public int readShort() throws IOException {
        return codedInputStream.readInt32();
    }

    @Override
    public int readByte() throws IOException {
        return codedInputStream.readInt32();
    }

    @Override
    public int readChar() throws IOException {
        return codedInputStream.readInt32();
    }

    @Override
    public int readInt() throws IOException {
        return codedInputStream.readInt32();
    }

    @Override
    public long readLong() throws IOException {
        return codedInputStream.readInt64();
    }

    @Override
    public float readFloat() throws IOException {
        return codedInputStream.readFloat();
    }

    @Override
    public double readDouble() throws IOException {
        return codedInputStream.readDouble();
    }

    @Override
    public String readString() throws IOException {
        return codedInputStream.readStringRequireUtf8();
    }
}
