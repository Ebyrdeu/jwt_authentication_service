package dev.ebyrdeu;

import dev.ebyrdeu.domain.dto.UserDto;
import dev.ebyrdeu.service.LoginService;
import dev.ebyrdeu.service.RegisterService;
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
    RegisterService registerService;

    @Inject
    LoginService loginService;

    @POST
    @Path("/signup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(UserDto dto) {
        return registerService.save(dto);
    }


    @POST
    @Path("/signin")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(UserDto dto) {
        return loginService.signIn(dto);
    }

}
