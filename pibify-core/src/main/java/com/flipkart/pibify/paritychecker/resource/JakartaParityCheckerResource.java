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

package com.flipkart.pibify.paritychecker.resource;

import com.flipkart.pibify.codegen.stub.AbstractPibifyHandlerCache;
import com.flipkart.pibify.codegen.stub.PibifyGenerated;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

/**
 * This resource is exposed to help visualize the proto returned from the server.
 * The proto can be sent back to server along with the FQDN of the class to deserialize the proto.
 * Author bageshwar.pn
 * Date 15/12/24
 */
@Path("/pibify/paritychecker")
public class JakartaParityCheckerResource {

    private final AbstractPibifyHandlerCache cache;

    public JakartaParityCheckerResource(AbstractPibifyHandlerCache cache) {
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
