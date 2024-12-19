package com.flipkart.pibify;

import com.flipkart.pibify.codegen.stub.AbstractPibifyHandlerCache;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 04/10/24
 */
public class PibifyHandlerCacheImpl extends AbstractPibifyHandlerCache {

    private static final PibifyHandlerCacheImpl INSTANCE;

    static {
        INSTANCE = new PibifyHandlerCacheImpl();
    }

    public PibifyHandlerCacheImpl() {
        packMap();
    }

    public static PibifyHandlerCacheImpl getInstance() {
        return INSTANCE;
    }
}
