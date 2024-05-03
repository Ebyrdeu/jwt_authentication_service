package dev.ebyrdeu;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.time.LocalDateTime;
import java.util.UUID;

@Path("/hello")
public class ExampleResource {
    private static final UUID uuid = UUID.randomUUID();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Quarkus REST";
    }


    @GET
    @Path("/user")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser() {
        return new User(uuid, LocalDateTime.now());
    }
}
