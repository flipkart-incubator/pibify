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
