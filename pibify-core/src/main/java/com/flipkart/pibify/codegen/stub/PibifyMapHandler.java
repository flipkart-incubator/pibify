package com.flipkart.pibify.codegen.stub;

import com.flipkart.pibify.codegen.PibifyCodeExecException;
import com.flipkart.pibify.serde.IDeserializer;
import com.flipkart.pibify.serde.ISerializer;
import com.flipkart.pibify.serde.PibifyDeserializer;
import com.flipkart.pibify.serde.PibifySerializer;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This class is used for serializing maps. This handler will trigger if the reference type in source code is map.
 * The handler will emit a warning suggesting the user to not use Object type references to hold maps, due to a
 * performance penalty here.
 * Author bageshwar.pn
 * Date 06/11/24
 */
@SuppressWarnings({"rawtypes", "OptionalGetWithoutIsPresent", "unchecked"})
public class PibifyMapHandler extends PibifyGenerated<Map> {

    private static final Logger logger = Logger.getLogger(PibifyMapHandler.class.getName());

    // Cache to look-up for the handler at runtime
    private final AbstractPibifyHandlerCache pibifyHandlerCache;

    public PibifyMapHandler(AbstractPibifyHandlerCache pibifyHandlerCache) {
        this.pibifyHandlerCache = pibifyHandlerCache;
    }

    @Override
    public byte[] serialize(Map object) throws PibifyCodeExecException {
        if (object == null) {
            return null;
        }

        logger.fine("Serializing via PibifyMapHandler, consider moving away from Object References for Maps");
        ISerializer serializer = new PibifySerializer();
        PibifyGenerated<Object> handler = pibifyHandlerCache.getHandler(Object.class).get();
        try {
            object.forEach((k, v) -> {
                try {
                    serializer.writeObjectAsBytes(1, handler.serialize(k));
                    serializer.writeObjectAsBytes(2, handler.serialize(v));
                } catch (IOException | PibifyCodeExecException e) {
                    throw new RuntimeException(e);
                }
            });

            return serializer.serialize();
        } catch (Exception e) {
            throw new PibifyCodeExecException(e);
        }
    }

    @Override
    public Map deserialize(byte[] bytes, Class<Map> clazz) throws
            PibifyCodeExecException {
        try {
            logger.fine("Deserializing via PibifyMapHandler, consider moving away from Object References for Maps");
            IDeserializer deserializer = new PibifyDeserializer(bytes);
            int tag = deserializer.getNextTag();
            Map object = clazz.getDeclaredConstructor().newInstance();
            PibifyGenerated<Object> handler = pibifyHandlerCache.getHandler(Object.class).get();
            Object key;
            Object value;
            while (tag != 0) {
                key = handler.deserialize(deserializer.readObjectAsBytes(), java.lang.Object.class);
                key = ((Map.Entry<String, Object>) (key)).getValue();
                tag = deserializer.getNextTag();
                value = handler.deserialize(deserializer.readObjectAsBytes(), java.lang.Object.class);
                value = ((Map.Entry<String, Object>) (value)).getValue();
                object.put(key, value);
                tag = deserializer.getNextTag();
            }
            return object;
        } catch (Exception e) {
            throw new PibifyCodeExecException(e);
        }
    }
}
