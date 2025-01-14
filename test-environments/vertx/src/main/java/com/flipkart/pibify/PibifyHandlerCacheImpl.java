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
