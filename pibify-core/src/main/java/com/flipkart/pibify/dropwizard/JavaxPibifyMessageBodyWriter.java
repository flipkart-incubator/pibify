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

package com.flipkart.pibify.dropwizard;

import com.flipkart.pibify.codegen.PibifyCodeExecException;
import com.flipkart.pibify.codegen.stub.AbstractPibifyHandlerCache;
import com.flipkart.pibify.codegen.stub.PibifyGenerated;
import com.flipkart.pibify.sampler.AbstractPibifySampler;

import javax.inject.Inject;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Optional;

/**
 * This class is used to intercept the `Accept` header to emit pibified binary instead of json
 * in a default dropwizard setup
 * Author bageshwar.pn
 * Date 02/12/24
 */
@Produces({"application/proto"})
public class JavaxPibifyMessageBodyWriter implements MessageBodyWriter<Object> {

    private final AbstractPibifyHandlerCache handlerCache;
    private final AbstractPibifySampler sampler;
    @Inject
    private javax.ws.rs.ext.Providers providers;

    public JavaxPibifyMessageBodyWriter(AbstractPibifyHandlerCache handlerCache, AbstractPibifySampler sampler) {
        this.handlerCache = handlerCache;
        this.sampler = sampler;
    }

    public JavaxPibifyMessageBodyWriter(AbstractPibifyHandlerCache handlerCache) {
        this(handlerCache, AbstractPibifySampler.DEFAULT_SAMPLER);
    }

    @Override
    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, javax.ws.rs.core.MediaType mediaType) {
        return true;
    }

    @Override
    public long getSize(Object o, Class<?> aClass, Type type, Annotation[] annotations, javax.ws.rs.core.MediaType mediaType) {
        return 0;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void writeTo(Object object, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream outputStream) throws IOException, javax.ws.rs.WebApplicationException {

        if (sampler.shouldSample()) {
            Optional<? extends PibifyGenerated> handler = handlerCache.getHandler(aClass);
            if (handler.isPresent()) {
                try {
                    outputStream.write(handler.get().serialize(object));
                } catch (PibifyCodeExecException e) {
                    throw new WebApplicationException(e);
                }
            } else {
                throw new WebApplicationException("Handler missing for class " + aClass.getName());
            }
        } else {
            MessageBodyWriter writer = getDefaultWriter(aClass, type, mediaType);
            if (writer != null) {
                httpHeaders.putSingle("Content-Type", MediaType.APPLICATION_JSON);
                writer.writeTo(object, aClass, type, annotations, mediaType, httpHeaders, outputStream);
            } else {
                throw new WebApplicationException("No suitable writer found for type: " + type);
            }
        }
    }

    private MessageBodyWriter<?> getDefaultWriter(Class<?> type, Type genericType, MediaType mediaType) {
        return providers.getMessageBodyWriter(type, genericType, new Annotation[0], MediaType.APPLICATION_JSON_TYPE);
    }
}
