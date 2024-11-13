package com.flipkart.pibify.codegen.stub;

import com.google.common.collect.ImmutableMap;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

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

        PibifyMapHandler mapHandler = new PibifyMapHandler(this);
        mapBuilder.put(Map.class, mapHandler);
        mapBuilder.put(HashMap.class, mapHandler);
        mapBuilder.put(TreeMap.class, mapHandler);
        mapBuilder.put(LinkedHashMap.class, mapHandler);

        PibifyCollectionHandler collectionHandler = new PibifyCollectionHandler(this);
        mapBuilder.put(ArrayList.class, collectionHandler);
        mapBuilder.put(Vector.class, collectionHandler);
        mapBuilder.put(ArrayDeque.class, collectionHandler);
        mapBuilder.put(Stack.class, collectionHandler);

        mapBuilder.put(HashSet.class, collectionHandler);
        mapBuilder.put(TreeSet.class, collectionHandler);
        mapBuilder.put(LinkedHashSet.class, collectionHandler);

        mapBuilder.put(BigDecimal.class, new BigDecimalHandler());
    }

    protected void packMap() {
        cache = mapBuilder.build();
    }

    protected void initializeHandlers() {
        cache.values().forEach(PibifyGenerated::initialize);
    }

    public <T> Optional<PibifyGenerated<T>> getHandler(Class<T> clazz) {

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
