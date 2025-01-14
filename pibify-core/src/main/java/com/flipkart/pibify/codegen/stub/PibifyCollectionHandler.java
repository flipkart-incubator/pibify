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

import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This class is used for serializing Collections. This handler will trigger if the reference type in source code is Collections.
 * The handler will emit a warning suggesting the user to not use Object type references to hold Collections, due to a
 * performance penalty here.
 * Author bageshwar.pn
 * Date 06/11/24
 */
@SuppressWarnings({"rawtypes", "OptionalGetWithoutIsPresent", "unchecked"})
public class PibifyCollectionHandler extends PibifyGenerated<Collection> {

    private static final Logger logger = Logger.getLogger(PibifyCollectionHandler.class.getName());

    private PibifyGenerated<Object> valueHandler;

    @Override
    public void serialize(Collection object, ISerializer serializer, SerializationContext context) throws PibifyCodeExecException {
        if (object == null) {
            return;
        }

        logger.fine("Serializing via PibifyCollectionHandler, consider moving away from Object References for Collections");
        try {
            for (java.lang.Object value : object) {
                serializer.writeObject(1, valueHandler, value, context);
            }
        } catch (Exception e) {
            throw new PibifyCodeExecException(e);
        }
    }

    @Override
    public Collection deserialize(IDeserializer deserializer, Class<Collection> clazz, SerializationContext context) throws PibifyCodeExecException {
        try {
            logger.fine("Deserializing via PibifyCollectionHandler, consider moving away from Object References for Collections");
            int tag = deserializer.getNextTag();
            Collection object = clazz.getDeclaredConstructor().newInstance();
            Object value;
            while (tag != 0 && tag != PibifyGenerated.getEndObjectTag()) {
                value = valueHandler.deserialize(deserializer, java.lang.Object.class, context);
                value = ((Map.Entry<String, Object>) (value)).getValue();
                object.add(value);
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
        valueHandler = pibifyHandlerCache.getHandler(Object.class).get();
    }
}
