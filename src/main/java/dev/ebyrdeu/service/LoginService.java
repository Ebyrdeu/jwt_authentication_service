package dev.ebyrdeu.service;

import dev.ebyrdeu.domain.dto.UserDto;
import dev.ebyrdeu.domain.entity.User;
import dev.ebyrdeu.domain.hashing.VerifyHashReq;
import dev.ebyrdeu.domain.hashing.VerifyHashRes;
import dev.ebyrdeu.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Slf4j
@ApplicationScoped
public class LoginService {

    @ConfigProperty(name = "password.hashing.rounds", defaultValue = "10")
    Integer rounds;

    @Inject
    TokenService tokenService;

    @Inject
    UserRepository userRepository;

    @Inject
    @RestClient
    SaltService saltService;

    @Transactional
    public Response signIn(UserDto dto) {
        User existingUser = findByUsername(dto.getUsername());
        boolean passwordVerification = verifyPassword(dto.getPassword(), existingUser);


        if (passwordVerification) {
            String jwToken = getJWToken(existingUser);
            return Response.ok(jwToken).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    private String getJWToken(User user) {
        return tokenService.generateToken(user.getIp(), user.getUsername());
    }

    private User findByUsername(String username) {
        try {
            log.debug("Searching for user by username: {}", username);
            return userRepository.findByUsername(username);
        } catch (NotFoundException e) {
            log.error("Failed to retrieve user by username: {}", username, e);
            throw new NotFoundException(e);
        }
    }


    private Boolean verifyPassword(String password, User existingUser) {
        log.debug("Verifying password for user: {}", existingUser.getUsername());
        VerifyHashReq req = new VerifyHashReq(password, rounds, existingUser.getSalt());
        VerifyHashRes res = saltService.verifySalt(existingUser.getPassword(), req);
        return res.getIs_equal();
    }
}
