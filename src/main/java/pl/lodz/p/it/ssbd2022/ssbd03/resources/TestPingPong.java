package pl.lodz.p.it.ssbd2022.ssbd03.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("ping")
public class TestPingPong {
    @GET
    public Response ping() {
        return Response.ok("pong").build();
    }

}

