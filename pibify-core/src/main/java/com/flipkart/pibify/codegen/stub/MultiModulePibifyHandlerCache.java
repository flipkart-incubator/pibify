package com.flipkart.pibify.codegen.stub;

/**
 * This class can combine PibifyHandlerCache from different modules into 1 common cache object
 * Author bageshwar.pn
 * Date 10/12/24
 */
public class MultiModulePibifyHandlerCache extends AbstractPibifyHandlerCache {

    public MultiModulePibifyHandlerCache(AbstractPibifyHandlerCache... caches) {
        // When this method is called, the concrete instance of cache are available
        // as per static block by this time, the cache has been backed and the handlers have been initialized
        // Hence just consolidating the maps here.
        for (AbstractPibifyHandlerCache pibifyCache : caches) {
            mapBuilder.putAll(pibifyCache.cache);
        }

        packMap(false);
    }
}
