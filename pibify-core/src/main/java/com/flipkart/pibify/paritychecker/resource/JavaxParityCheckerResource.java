package com.flipkart.pibify.paritychecker.resource;

import com.flipkart.pibify.codegen.stub.AbstractPibifyHandlerCache;
import com.flipkart.pibify.codegen.stub.PibifyGenerated;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

/**
 * This resource is exposed to help visualize the proto returned from the server.
 * The proto can be sent back to server along with the FQDN of the class to deserialize the proto.
 * Author bageshwar.pn
 * Date 15/12/24
 */
@Path("/pibify/paritychecker")
public class JavaxParityCheckerResource {

    private final AbstractPibifyHandlerCache cache;

    public JavaxParityCheckerResource(AbstractPibifyHandlerCache cache) {
        this.cache = cache;
    }

    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_OCTET_STREAM})
    @POST
    public Response checkParity(@QueryParam("fqdn") String fqdn, byte[] payload) throws ClassNotFoundException {
        if (fqdn == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("fqdn is required").build();
        }

        Optional<? extends PibifyGenerated<?>> handler = cache.getHandler(Class.forName(fqdn));
        if (!handler.isPresent()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Handler missing for class " + fqdn).build();
        }

        try {
            Object object = handler.get().deserialize(payload);
            return Response.ok(object).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to deserialize payload").build();
        }
    }
}
