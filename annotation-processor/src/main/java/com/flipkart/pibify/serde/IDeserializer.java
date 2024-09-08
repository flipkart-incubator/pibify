package com.flipkart.pibify.serde;

import java.io.IOException;

/**
 * Interface for a deserializer
 * Author bageshwar.pn
 * Date 27/07/24
 */
public interface IDeserializer {
    Object deserialize(byte[] bytes) throws IOException;

    int getNextTag() throws IOException;

    boolean readBool() throws IOException;

    int readShort() throws IOException;

    int readByte() throws IOException;

    int readChar() throws IOException;

    int readInt() throws IOException;

    long readLong() throws IOException;

    float readFloat() throws IOException;

    double readDouble() throws IOException;

    String readString() throws IOException;

    byte[] readObjectAsBytes() throws IOException;

    int readEnum() throws IOException;
}
