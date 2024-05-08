package dev.ebyrdeu;

import dev.ebyrdeu.domain.dto.UserDto;
import dev.ebyrdeu.service.RegisterService;
import dev.ebyrdeu.service.TokenService;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Path("/api/auth")
@Slf4j
@RequestScoped
public class AuthResource {


    @Inject
    TokenService tokenService;

    @Inject
    RegisterService registerService;

    @POST
    @Path("/signup")
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(UserDto dto) {
        return registerService.save(dto);
    }

    @GET
    @PermitAll
    @Path("/signin")
    @Produces(MediaType.TEXT_PLAIN)
    public String login(@QueryParam("ip") String ip, @QueryParam("username") String username) {
        log.info("Attempting to login user {}", username);
        return tokenService.generateToken(ip, username);
    }

}
