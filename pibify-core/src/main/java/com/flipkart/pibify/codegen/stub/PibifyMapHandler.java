package com.flipkart.pibify.codegen.stub;

import com.flipkart.pibify.codegen.PibifyCodeExecException;
import com.flipkart.pibify.serde.IDeserializer;
import com.flipkart.pibify.serde.ISerializer;

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
    private PibifyGenerated<Object> objectHandler;

    public PibifyMapHandler(AbstractPibifyHandlerCache pibifyHandlerCache) {
        this.pibifyHandlerCache = pibifyHandlerCache;
    }

    @Override
    public void serialize(Map object, ISerializer serializer) throws PibifyCodeExecException {
        if (object == null) {
            return;
        }

        logger.fine("Serializing via PibifyMapHandler, consider moving away from Object References for Maps");
        try {

            if (objectHandler == null) {
                objectHandler = this.pibifyHandlerCache.getHandler(Object.class).get();
            }

            for (Object entryObj : object.entrySet()) {
                Map.Entry entry = (Map.Entry) entryObj;
                serializer.writeObject(1, objectHandler, entry.getKey());
                serializer.writeObject(2, objectHandler, entry.getValue());
            }
        } catch (Exception e) {
            throw new PibifyCodeExecException(e);
        }
    }

    @Override
    public Map deserialize(IDeserializer deserializer, Class<Map> clazz) throws
            PibifyCodeExecException {
        try {
            logger.fine("Deserializing via PibifyMapHandler, consider moving away from Object References for Maps");
            int tag = deserializer.getNextTag();
            Map object = clazz.getDeclaredConstructor().newInstance();
            PibifyGenerated<Object> handler = pibifyHandlerCache.getHandler(Object.class).get();
            Object key;
            Object value;
            while (tag != 0 && tag != PibifyGenerated.getEndObjectTag()) {
                key = handler.deserialize(deserializer, java.lang.Object.class);
                key = ((Map.Entry<String, Object>) (key)).getValue();
                tag = deserializer.getNextTag();
                value = handler.deserialize(deserializer, java.lang.Object.class);
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
