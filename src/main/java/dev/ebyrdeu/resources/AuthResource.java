package dev.ebyrdeu.resources;

import dev.ebyrdeu.service.TokenService;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/api/auth")
@RequestScoped
public class AuthResource {

    @Inject
    TokenService tokenService;

    @POST
    @Path("/singup")
    @Transactional
    public void register() {

    }

    @GET
    @PermitAll
    @Path("/signin")
    @Produces(MediaType.TEXT_PLAIN)
    public String login(@QueryParam("email") String email, @QueryParam("username") String username) {
        return tokenService.generateToken(email, username);
    }


}
