package com.flipkart.pibify.paritychecker.filter;

/**
 * Parity Filter for Jakarta based webapps
 * Author bageshwar.pn
 * Date 12/12/24
 */

import com.flipkart.pibify.paritychecker.IParityChecker;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ext.Provider;

@Provider
public class JakartaJsonResponseFilter extends AbstractJsonResponseFilter<ContainerRequestContext, ContainerResponseContext>
        implements ContainerResponseFilter {

    public JakartaJsonResponseFilter(IParityChecker parityChecker) {
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
