package com.flipkart.pibify.codegen.stub;

import com.flipkart.pibify.codegen.PibifyCodeExecException;
import com.flipkart.pibify.serde.IDeserializer;
import com.flipkart.pibify.serde.ISerializer;
import com.flipkart.pibify.serde.PibifyDeserializer;
import com.flipkart.pibify.serde.PibifySerializer;

import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This class is used for serializing Collections. This handler will trigger if the reference type in source code is Collections.
 * The handler will emit a warning suggesting the user to not use Object type references to hold Collections, due to a
 * performance penalty here.
 * Author bageshwar.pn
 * Date 06/11/24
 */
@SuppressWarnings({"rawtypes", "OptionalGetWithoutIsPresent", "unchecked"})
public class PibifyCollectionHandler extends PibifyGenerated<Collection> {

    private static final Logger logger = Logger.getLogger(PibifyCollectionHandler.class.getName());

    // Cache to look-up for the handler at runtime
    private final AbstractPibifyHandlerCache pibifyHandlerCache;

    public PibifyCollectionHandler(AbstractPibifyHandlerCache pibifyHandlerCache) {
        this.pibifyHandlerCache = pibifyHandlerCache;
    }

    @Override
    public byte[] serialize(Collection object) throws PibifyCodeExecException {
        if (object == null) {
            return null;
        }

        logger.fine("Serializing via PibifyCollectionHandler, consider moving away from Object References for Collections");

        ISerializer serializer = new PibifySerializer();
        PibifyGenerated<Object> valueHandler = pibifyHandlerCache.getHandler(Object.class).get();
        try {
            for (java.lang.Object value : object) {
                serializer.writeObjectAsBytes(1, valueHandler.serialize(value));
            }
            return serializer.serialize();
        } catch (Exception e) {
            throw new PibifyCodeExecException(e);
        }
    }

    @Override
    public Collection deserialize(byte[] bytes, Class<Collection> clazz) throws PibifyCodeExecException {
        try {
            logger.fine("Deserializing via PibifyCollectionHandler, consider moving away from Object References for Collections");
            IDeserializer deserializer = new PibifyDeserializer(bytes);
            int tag = deserializer.getNextTag();
            Collection object = clazz.getDeclaredConstructor().newInstance();
            PibifyGenerated<Object> valueHandler = pibifyHandlerCache.getHandler(Object.class).get();
            Object value;
            while (tag != 0) {
                value = valueHandler.deserialize(deserializer.readObjectAsBytes(), java.lang.Object.class);
                value = ((Map.Entry<String, Object>) (value)).getValue();
                object.add(value);
                tag = deserializer.getNextTag();
            }
            return object;
        } catch (Exception e) {
            throw new PibifyCodeExecException(e);
        }
    }
}
