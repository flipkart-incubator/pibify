package com.flipkart.pibify.dropwizard;

import com.flipkart.pibify.codegen.PibifyCodeExecException;
import com.flipkart.pibify.codegen.stub.AbstractPibifyHandlerCache;
import com.flipkart.pibify.codegen.stub.PibifyGenerated;
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
//@Provider Don't want this component to be discovered implicitly
@Produces({"application/proto"})
public class JakartaPibifyMessageBodyWriter implements MessageBodyWriter<Object> {

    private final AbstractPibifyHandlerCache handlerCache;

    public JakartaPibifyMessageBodyWriter(AbstractPibifyHandlerCache handlerCache) {
        this.handlerCache = handlerCache;
    }


    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        // accept everything
        return true;
    }


    @Override
    public void writeTo(Object object, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                        MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
            throws IOException, WebApplicationException {

        @SuppressWarnings("rawtypes")
        Optional<? extends PibifyGenerated> handler = handlerCache.getHandler(type);
        if (handler.isPresent()) {
            try {
                //noinspection unchecked
                entityStream.write(handler.get().serialize(object));
            } catch (PibifyCodeExecException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("Handler missing for class " + type.getName());
        }
    }
}
