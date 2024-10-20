package com.flipkart.pibify.codegen.stub;

import com.flipkart.pibify.codegen.PibifyCodeExecException;
import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.Optional;

/**
 * This class serves as a base class for PibifyHandlerCache.
 * The handler caches are meant to singleton to a specific maven module.
 * The generated `PibifyHandlerCache` is created once for maven module configuration.
 * Correspondingly all internal state fields of this class are also module-private.
 *
 * Author bageshwar.pn
 * Date 01/10/24
 */
public abstract class AbstractPibifyHandlerCache {

    protected final ImmutableMap.Builder<Class<?>, PibifyGenerated<?>> mapBuilder = ImmutableMap.builder();
    private static final PibifyGenerated<Object> objectMapperHandler;

    static {
        objectMapperHandler = new PibifyObjectHandlerViaObjectMapper();
    }

    protected Map<Class<?>, PibifyGenerated<?>> cache;

    protected AbstractPibifyHandlerCache() {
        mapBuilder.put(Object.class, new PibifyObjectHandler(this));
        packMap();
    }

    protected void packMap() {
        cache = mapBuilder.build();
    }

    public <T> Optional<PibifyGenerated<T>> getHandler(Class<T> clazz) throws PibifyCodeExecException {

        if (clazz == null) {
            return Optional.empty();
        }

        PibifyGenerated<?> pibifyGenerated = cache.get(clazz);

        // Defaulting to object mapper based handler if we don't find one in the cache.
        if (pibifyGenerated == null) {
            pibifyGenerated = objectMapperHandler;
        }

        //noinspection unchecked
        return Optional.of((PibifyGenerated<T>) pibifyGenerated);
    }
}
