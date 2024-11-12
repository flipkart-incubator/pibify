package com.flipkart.pibify.codegen.stub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.pibify.codegen.PibifyCodeExecException;
import com.flipkart.pibify.serde.IDeserializer;
import com.flipkart.pibify.serde.ISerializer;

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
    public void serialize(Object object, ISerializer serializer) throws PibifyCodeExecException {

        if (object == null) {
            return;
        }

        try {
            logger.fine("Serializing via ObjectMapper " + object.getClass().getName());
            serializer.writeObjectAsBytes(1, pibifyObjectMapper.writeValueAsBytes(object));

        } catch (IOException e) {
            throw new PibifyCodeExecException(e);
        }
    }

    @Override
    public Object deserialize(IDeserializer deserializer, Class<Object> type) throws PibifyCodeExecException {
        try {
            int nextTag = deserializer.getNextTag(); // consume start object tag
            Object deserialized = this.deserialize(deserializer.readObjectAsBytes(), type);
            nextTag = deserializer.getNextTag(); // consume end object tag
            return deserialized;
        } catch (IOException e) {
            throw new PibifyCodeExecException(e);
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<Object> type) throws PibifyCodeExecException {
        if (type == null) {
            throw new PibifyCodeExecException("Class Type cannot be null");
        }
        logger.fine("Deserializing via ObjectMapper " + type.getName());
        return pibifyObjectMapper.readObjectFromBytes(bytes, type);
    }
}
