package com.flipkart.pibify.dropwizard;

import com.flipkart.pibify.codegen.PibifyCodeExecException;
import com.flipkart.pibify.codegen.stub.AbstractPibifyHandlerCache;
import com.flipkart.pibify.codegen.stub.PibifyGenerated;
import com.flipkart.pibify.sampler.AbstractPibifySampler;
import jakarta.inject.Inject;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyWriter;

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
public class JakartaPibifyMessageBodyWriter implements MessageBodyWriter<Object> {

    private final AbstractPibifyHandlerCache handlerCache;
    private final AbstractPibifySampler sampler;

    @Inject
    private jakarta.ws.rs.ext.Providers providers;

    public JakartaPibifyMessageBodyWriter(AbstractPibifyHandlerCache handlerCache, AbstractPibifySampler sampler) {
        this.handlerCache = handlerCache;
        this.sampler = sampler;
    }


    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        // accept everything
        return true;
    }


    @SuppressWarnings("rawtypes")
    @Override
    public void writeTo(Object object, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                        MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
            throws IOException, WebApplicationException {

        if (sampler.shouldSample()) {
            Optional<? extends PibifyGenerated> handler = handlerCache.getHandler(type);
            if (handler.isPresent()) {
                try {
                    //noinspection unchecked
                    entityStream.write(handler.get().serialize(object));
                } catch (PibifyCodeExecException e) {
                    throw new WebApplicationException(e);
                }
            } else {
                throw new WebApplicationException("Handler missing for class " + type.getName());
            }
        } else {
            MessageBodyWriter writer = getDefaultWriter(type, genericType, mediaType);
            if (writer != null) {
                httpHeaders.putSingle("Content-Type", MediaType.APPLICATION_JSON);
                writer.writeTo(object, type, genericType, annotations, mediaType, httpHeaders, entityStream);
            } else {
                throw new WebApplicationException("No suitable writer found for type: " + type);
            }
        }
    }

    private MessageBodyWriter<?> getDefaultWriter(Class<?> type, Type genericType, MediaType mediaType) {
        return providers.getMessageBodyWriter(type, genericType, new Annotation[0], MediaType.APPLICATION_JSON_TYPE);
    }
}
