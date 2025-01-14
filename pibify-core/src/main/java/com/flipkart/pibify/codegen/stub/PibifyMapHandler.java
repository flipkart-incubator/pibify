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

import java.util.Map;
import java.util.logging.Logger;

/**
 * This class is used for serializing maps. This handler will trigger if the reference type in source code is map.
 * The handler will emit a warning suggesting the user to not use Object type references to hold maps, due to a
 * performance penalty here.
 * Author bageshwar.pn
 * Date 06/11/24
 */
@SuppressWarnings({"rawtypes", "OptionalGetWithoutIsPresent", "unchecked"})
public class PibifyMapHandler extends PibifyGenerated<Map> {

    private static final Logger logger = Logger.getLogger(PibifyMapHandler.class.getName());

    private PibifyGenerated<Object> objectHandler;

    @Override
    public void serialize(Map object, ISerializer serializer, SerializationContext context) throws PibifyCodeExecException {
        if (object == null) {
            return;
        }

        logger.fine("Serializing via PibifyMapHandler, consider moving away from Object References for Maps");
        try {
            for (Object entryObj : object.entrySet()) {
                Map.Entry entry = (Map.Entry) entryObj;
                serializer.writeObject(1, objectHandler, entry.getKey(), context);
                serializer.writeObject(2, objectHandler, entry.getValue(), context);
            }
        } catch (Exception e) {
            throw new PibifyCodeExecException(e);
        }
    }

    @Override
    public Map deserialize(IDeserializer deserializer, Class<Map> clazz, SerializationContext context) throws
            PibifyCodeExecException {
        try {
            logger.fine("Deserializing via PibifyMapHandler, consider moving away from Object References for Maps");
            int tag = deserializer.getNextTag();
            Map object = clazz.getDeclaredConstructor().newInstance();
            Object key;
            Object value;
            while (tag != 0 && tag != PibifyGenerated.getEndObjectTag()) {
                key = objectHandler.deserialize(deserializer, java.lang.Object.class, context);
                key = ((Map.Entry<String, Object>) (key)).getValue();
                tag = deserializer.getNextTag();
                value = objectHandler.deserialize(deserializer, java.lang.Object.class, context);
                value = ((Map.Entry<String, Object>) (value)).getValue();
                object.put(key, value);
                tag = deserializer.getNextTag();
            }
            return object;
        } catch (Exception e) {
            throw new PibifyCodeExecException(e);
        }
    }

    @Override
    public void initialize(AbstractPibifyHandlerCache pibifyHandlerCache) {
        super.initialize(pibifyHandlerCache);
        objectHandler = pibifyHandlerCache.getHandler(Object.class).get();
    }
}
