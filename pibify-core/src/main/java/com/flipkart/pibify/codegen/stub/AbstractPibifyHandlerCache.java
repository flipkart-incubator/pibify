package com.flipkart.pibify.codegen.stub;

import com.flipkart.pibify.codegen.PibifyCodeExecException;
import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.Optional;

/**
 * This class serves as a base class for PibifyHandlerCache
 * Author bageshwar.pn
 * Date 01/10/24
 */
public abstract class AbstractPibifyHandlerCache {

    protected static final ImmutableMap.Builder<Class<?>, PibifyGenerated<?>> mapBuilder = ImmutableMap.builder();
    private static final PibifyGenerated<Object> objectMapperHandler;

    static {
        mapBuilder.put(Object.class, new PibifyObjectHandler());
        objectMapperHandler = new PibifyObjectHandlerViaObjectMapper();
    }

    protected Map<Class<?>, PibifyGenerated<?>> cache;

    protected AbstractPibifyHandlerCache() {
        packMap();
    }

    protected void packMap() {
        cache = mapBuilder.build();
    }

    public <T> Optional<PibifyGenerated<T>> getHandler(Class<T> clazz) throws PibifyCodeExecException {

        if (clazz == null) {
            return Optional.empty();
        }

        if (!cache.containsKey(clazz)) {
            return Optional.of((PibifyGenerated<T>) objectMapperHandler);
        }

        //noinspection unchecked
        return Optional.of((PibifyGenerated<T>) cache.get(clazz));
    }
}
