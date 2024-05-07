package dev.ebyrdeu.resources;

import dev.ebyrdeu.service.TokenService;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/api/auth")
@RequestScoped
public class AuthResource {

    private static final Logger log = LoggerFactory.getLogger(AuthResource.class);

    @Inject
    TokenService tokenService;

    @POST
    @Path("/singup")
    @Transactional
    public void register() {
        log.info("Registering new user");
    }

    @GET
    @PermitAll
    @Path("/signin")
    @Produces(MediaType.TEXT_PLAIN)
    public String login(@QueryParam("email") String email, @QueryParam("username") String username) {
        log.info("Attempting to login user {}", username);
        return tokenService.generateToken(email, username);
    }


}
