/*
 *
 *  *Copyright [2025] [Original Author]
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.flipkart.pibify.codegen.stub;

import com.google.common.collect.ImmutableMap;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
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
    protected static final PibifyGenerated<Object> objectMapperHandler;

    static {
        objectMapperHandler = new PibifyObjectHandlerViaObjectMapper();
    }

    protected Map<Class<?>, PibifyGenerated<?>> cache;

    /**
     * Initializes the handler cache by registering handlers for various data types.
     *
     * This constructor populates the {@code mapBuilder} with default handlers for:
     * <ul>
     *   <li>Base object types: {@code Object}</li>
     *   <li>Map implementations: {@code Map}, {@code HashMap}, {@code TreeMap}, {@code LinkedHashMap}</li>
     *   <li>Collection implementations: {@code ArrayList}, {@code Vector}, {@code ArrayDeque}, {@code Stack}</li>
     *   <li>Set implementations: {@code HashSet}, {@code TreeSet}, {@code LinkedHashSet}</li>
     *   <li>Special types: {@code BigDecimal}, {@code Date}, {@code SerializationContext}</li>
     * </ul>
     *
     * Each type is associated with an appropriate handler to manage serialization and deserialization.
     *
     * @see PibifyObjectHandler
     * @see PibifyMapHandler
     * @see PibifyCollectionHandler
     * @see BigDecimalHandler
     * @see DateHandler
     * @see SerializationContextHandler
     */
    protected AbstractPibifyHandlerCache() {
        mapBuilder.put(Object.class, new PibifyObjectHandler(this));

        PibifyMapHandler mapHandler = new PibifyMapHandler();
        mapBuilder.put(Map.class, mapHandler);
        mapBuilder.put(HashMap.class, mapHandler);
        mapBuilder.put(TreeMap.class, mapHandler);
        mapBuilder.put(LinkedHashMap.class, mapHandler);

        PibifyCollectionHandler collectionHandler = new PibifyCollectionHandler();
        mapBuilder.put(ArrayList.class, collectionHandler);
        mapBuilder.put(Vector.class, collectionHandler);
        mapBuilder.put(ArrayDeque.class, collectionHandler);
        mapBuilder.put(Stack.class, collectionHandler);

        mapBuilder.put(HashSet.class, collectionHandler);
        mapBuilder.put(TreeSet.class, collectionHandler);
        mapBuilder.put(LinkedHashSet.class, collectionHandler);

        mapBuilder.put(BigDecimal.class, new BigDecimalHandler());
        mapBuilder.put(Date.class, new DateHandler());
        mapBuilder.put(SerializationContext.class, new SerializationContextHandler());
    }

    /**
     * Builds the handler cache map with optional duplicate handling behavior.
     *
     * @param throwIfDuplicates Determines the strategy for handling duplicate keys during map construction
     *                           - If true, throws an exception when duplicate keys are encountered
     *                           - If false, keeps the last encountered value for duplicate keys
     */
    protected void packMap(boolean throwIfDuplicates) {
        if (throwIfDuplicates) {
            cache = mapBuilder.buildOrThrow();
        } else {
            cache = mapBuilder.buildKeepingLast();
        }
    }

    protected void packMap() {
        packMap(true);
    }

    protected void initializeHandlers() {
        cache.values().forEach(this::initializeHandler);
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

    /**
     * This helper method lets clients get an instance of the concrete implementation of the cache.
     * It's needed to let the clients avoid compilation errors,
     * which can happen when the pibify sources have not been generated
     *
     * @param fqdn
     * @return
     */
    public static AbstractPibifyHandlerCache getConcreteInstance(String fqdn) {
        try {
            return (AbstractPibifyHandlerCache) Class.forName(fqdn).getMethod("getInstance").invoke(null);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 ClassNotFoundException e) {
            throw new RuntimeException("Unable to instantiate " + fqdn, e);
        }
    }

    private void initializeHandler(PibifyGenerated<?> handler) {
        handler.initialize(this);
    }
}
