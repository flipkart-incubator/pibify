package com.flipkart.pibify.dropwizard;

import com.flipkart.pibify.codegen.PibifyCodeExecException;
import com.flipkart.pibify.codegen.stub.AbstractPibifyHandlerCache;
import com.flipkart.pibify.codegen.stub.PibifyGenerated;

import javax.ws.rs.Produces;
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
//@Provider Don't want this component to be discovered implicitly
@Produces({"application/proto"})
public class JavaxPibifyMessageBodyWriter implements MessageBodyWriter<Object> {

    private final AbstractPibifyHandlerCache handlerCache;

    public JavaxPibifyMessageBodyWriter(AbstractPibifyHandlerCache handlerCache) {
        this.handlerCache = handlerCache;
    }

    @Override
    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, javax.ws.rs.core.MediaType mediaType) {
        return true;
    }

    @Override
    public long getSize(Object o, Class<?> aClass, Type type, Annotation[] annotations, javax.ws.rs.core.MediaType mediaType) {
        return 0;
    }

    @Override
    public void writeTo(Object object, Class<?> aClass, Type type, Annotation[] annotations, javax.ws.rs.core.MediaType mediaType, javax.ws.rs.core.MultivaluedMap<String, Object> multivaluedMap, OutputStream outputStream) throws IOException, javax.ws.rs.WebApplicationException {
        @SuppressWarnings("rawtypes")
        Optional<? extends PibifyGenerated> handler = handlerCache.getHandler(aClass);
        if (handler.isPresent()) {
            try {
                //noinspection unchecked
                outputStream.write(handler.get().serialize(object));
            } catch (PibifyCodeExecException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("Handler missing for class " + aClass.getName());
        }
    }
}
