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
            INSTANCE.initializeHandlers();
        }
        return INSTANCE;
    }

    public static <T> void addEntry(Class<T> type, PibifyGenerated<T> handler) {
        if (!getInstance().cache.containsKey(type)) {
            getInstance().mapBuilder.put(type, handler);
            handler.initialize(getInstance());
            getInstance().packMap();
        }
    }
}
