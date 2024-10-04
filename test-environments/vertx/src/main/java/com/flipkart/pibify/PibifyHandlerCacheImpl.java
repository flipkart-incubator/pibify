package com.flipkart.pibify;

import com.flipkart.pibify.codegen.PibifyCodeExecException;
import com.flipkart.pibify.codegen.stub.AbstractPibifyHandlerCache;
import com.flipkart.pibify.codegen.stub.PibifyGenerated;

import java.util.ArrayList;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 04/10/24
 */
public class PibifyHandlerCacheImpl extends AbstractPibifyHandlerCache {

    private static final PibifyHandlerCacheImpl INSTANCE;

    static {
        mapBuilder.put(ArrayList.class, new PibifyGenerated<ArrayList>() {
            @Override
            public byte[] serialize(ArrayList object) throws PibifyCodeExecException {
                return "hello".getBytes();
            }

            @Override
            public ArrayList deserialize(byte[] bytes) throws PibifyCodeExecException {
                return null;
            }
        });
        INSTANCE = new PibifyHandlerCacheImpl();
    }

    public static PibifyHandlerCacheImpl getInstance() {
        return INSTANCE;
    }
}
