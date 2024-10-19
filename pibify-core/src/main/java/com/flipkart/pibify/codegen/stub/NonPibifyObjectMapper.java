package com.flipkart.pibify.codegen.stub;

/**
 * This interface is designed for plugging in a custom object mapper
 * Author bageshwar.pn
 * Date 18/10/24
 */
public interface NonPibifyObjectMapper {

    byte[] writeValueAsBytes(Object object);

    <T> T readObjectFromBytes(byte[] bytes, Class<T> valueType);
}
