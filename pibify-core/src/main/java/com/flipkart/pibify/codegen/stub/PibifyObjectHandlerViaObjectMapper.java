package com.flipkart.pibify.codegen.stub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.pibify.codegen.PibifyCodeExecException;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * This class is used for serde of non-pibify classes
 * Author bageshwar.pn
 * Date 17/10/24
 */
public class PibifyObjectHandlerViaObjectMapper extends PibifyGenerated<Object> {

    private final NonPibifyObjectMapper pibifyObjectMapper;
    private static final Logger logger = Logger.getLogger(PibifyObjectHandlerViaObjectMapper.class.getName());

    public PibifyObjectHandlerViaObjectMapper(NonPibifyObjectMapper pibifyObjectMapper) {
        this.pibifyObjectMapper = pibifyObjectMapper;
    }

    public PibifyObjectHandlerViaObjectMapper() {
        this(new NonPibifyObjectMapper() {
            final ObjectMapper objectMapper = new ObjectMapper();

            @Override
            public byte[] writeValueAsBytes(Object object) {
                try {
                    return objectMapper.writeValueAsBytes(object);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public <T> T readObjectFromBytes(byte[] bytes, Class<T> valueType) {
                try {
                    return objectMapper.readValue(bytes, valueType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public byte[] serialize(Object object) throws PibifyCodeExecException {
        if (object == null) {
            return null;
        }

        logger.fine("Serializing via ObjectMapper " + object.getClass().getName());
        return pibifyObjectMapper.writeValueAsBytes(object);
    }

    @Override
    public Object deserialize(byte[] bytes, Class type) throws PibifyCodeExecException {
        if (type == null) {
            throw new PibifyCodeExecException("Class Type cannot be null");
        }
        logger.fine("Deserializing via ObjectMapper " + type.getName());
        return pibifyObjectMapper.readObjectFromBytes(bytes, type);
    }
}
