package com.flipkart.pibify.serde;

import java.io.IOException;

/**
 * Interface for a serializer
 * Author bageshwar.pn
 * Date 27/07/24
 */
public interface ISerializer {
    byte[] serialize() throws IOException;

    void writeBool(int index, boolean value) throws IOException;

    void writeShort(int index, short value) throws IOException;

    void writeByte(int index, byte value) throws IOException;

    void writeChar(int index, char value) throws IOException;

    void writeInt(int index, int value) throws IOException;

    void writeLong(int index, long value) throws IOException;

    void writeFloat(int index, float value) throws IOException;

    void writeDouble(int index, double value) throws IOException;

    void writeString(int index, String value) throws IOException;

    void writeObjectAsBytes(int index, byte[] value) throws IOException;
}
