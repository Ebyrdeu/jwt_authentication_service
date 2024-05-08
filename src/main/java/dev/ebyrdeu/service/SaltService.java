package dev.ebyrdeu.service;

import dev.ebyrdeu.domain.hashing.AutoHashReq;
import dev.ebyrdeu.domain.hashing.AutoHashRes;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient
public interface SaltService {
    @POST
    @Path("/sha256/method/auto")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    AutoHashRes createSalt(AutoHashReq req);

//
//    @POST
//    @Path("sha256/{password}")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    VerifyHashRes verifySalt(@PathParam("password") String hashedPassword, VerifyHashReq req);
}
