package com.flipkart.pibify.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * This class is used for test
 * Author bageshwar.pn
 * Date 02/12/24
 */


@Path("/")
public class SampleResource {

    @Path("/sample")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public SampleResponse getSampleResponse() {
        return new SampleResponse(System.currentTimeMillis(), "Hello World");
    }
}
