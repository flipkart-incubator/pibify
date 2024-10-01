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
    protected final Map<Class<?>, PibifyGenerated<?>> cache;

    protected AbstractPibifyHandlerCache() {
        cache = packMap();
    }

    protected Map<Class<?>, PibifyGenerated<?>> packMap() {
        return mapBuilder.build();
    }

    public <T> Optional<PibifyGenerated<T>> getHandler(Class<T> clazz) throws PibifyCodeExecException {

        if (clazz == null || !cache.containsKey(clazz)) {
            return Optional.empty();
        }

        //noinspection unchecked
        return Optional.of((PibifyGenerated<T>) cache.get(clazz));
    }
}
