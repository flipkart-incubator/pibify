package com.flipkart.pibify.paritychecker.filter;

/**
 * Parity Filter for javax.ws.rs based webapps
 * Author bageshwar.pn
 * Date 12/12/24
 */

import com.flipkart.pibify.paritychecker.IParityChecker;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

@Provider
public class JavaxJsonResponseFilter extends AbstractJsonResponseFilter<ContainerRequestContext, ContainerResponseContext>
        implements ContainerResponseFilter {

    public JavaxJsonResponseFilter(IParityChecker parityChecker) {
        super(parityChecker);
    }

    @Override
    protected boolean isJsonResponse(ContainerResponseContext responseContext) {
        MediaType mediaType = responseContext.getMediaType();
        return mediaType != null &&
                MediaType.APPLICATION_JSON_TYPE.isCompatible(mediaType);
    }

    @Override
    protected Object getResponseEntity(ContainerResponseContext responseContext) {
        return responseContext.getEntity();
    }
}
