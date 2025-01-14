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

import com.flipkart.pibify.codegen.PibifyCodeExecException;
import com.flipkart.pibify.serde.IDeserializer;
import com.flipkart.pibify.serde.ISerializer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MultiModulePibifyHandlerCacheTest {


    private static SampleResponseHandler2 getSampleResponseHandler2() {
        return new SampleResponseHandler2();
    }

    @Test
    void testMultiModule() {
        SampleResponseHandler1 handler1 = new SampleResponseHandler1();
        SampleResponseHandler2 handler2 = getSampleResponseHandler2();
        MultiModulePibifyHandlerCache cache = new MultiModulePibifyHandlerCache(
                new PibifyHandlerCache1(handler1), new PibifyHandlerCache2(handler2));
        assertEquals(handler1, cache.getHandler(SampleResponse1.class).get());
        assertEquals(handler2, cache.getHandler(SampleResponse2.class).get());
        assertNotNull(cache.getHandler(SampleResponse3.class));
    }

    static class SampleResponse1 {
    }

    static class SampleResponse2 {
    }

    static class SampleResponse3 {
    }

    static class SampleResponseHandler1 extends PibifyGenerated<SampleResponse1> {

        @Override
        public void serialize(SampleResponse1 object, ISerializer serializer, SerializationContext context) throws PibifyCodeExecException {

        }

        @Override
        public SampleResponse1 deserialize(IDeserializer deserializer, Class<SampleResponse1> type, SerializationContext context) throws PibifyCodeExecException {
            return null;
        }
    }

    static class SampleResponseHandler2 extends PibifyGenerated<SampleResponse2> {

        @Override
        public void serialize(SampleResponse2 object, ISerializer serializer, SerializationContext context) throws PibifyCodeExecException {

        }

        @Override
        public SampleResponse2 deserialize(IDeserializer deserializer, Class<SampleResponse2> type, SerializationContext context) throws PibifyCodeExecException {
            return null;
        }
    }

    static class PibifyHandlerCache1 extends AbstractPibifyHandlerCache {
        protected PibifyHandlerCache1(PibifyGenerated sampleResponseHandler1) {
            mapBuilder.put(SampleResponse1.class, sampleResponseHandler1);
            packMap();
        }
    }

    static class PibifyHandlerCache2 extends AbstractPibifyHandlerCache {
        protected PibifyHandlerCache2(PibifyGenerated sampleResponseHandler2) {
            mapBuilder.put(SampleResponse2.class, sampleResponseHandler2);
            packMap();
        }
    }
}