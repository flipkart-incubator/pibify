package com.flipkart.pibify.test.util;

import com.flipkart.pibify.codegen.stub.AbstractPibifyHandlerCache;
import com.flipkart.pibify.codegen.stub.PibifyGenerated;

/**
 * This class is used for supporting the test by building a cache of handlers at the time of in-mem compilation
 * Author bageshwar.pn
 * Date 18/10/24
 */
public class PibifyHandlerCacheForTest extends AbstractPibifyHandlerCache {
    private static PibifyHandlerCacheForTest INSTANCE;

    private PibifyHandlerCacheForTest() {
        packMap();
    }

    public static PibifyHandlerCacheForTest getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PibifyHandlerCacheForTest();
        }
        return INSTANCE;
    }

    public static <T> void addEntry(Class<T> type, PibifyGenerated<T> handler) {
        if (!getInstance().cache.containsKey(type)) {
            getInstance().mapBuilder.put(type, handler);
            handler.initialize();
            getInstance().packMap();
        }
    }
}
