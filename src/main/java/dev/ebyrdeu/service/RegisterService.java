package dev.ebyrdeu.service;

import dev.ebyrdeu.domain.dto.UserDto;
import dev.ebyrdeu.domain.entity.User;
import dev.ebyrdeu.domain.hashing.AutoHashReq;
import dev.ebyrdeu.domain.hashing.AutoHashRes;
import dev.ebyrdeu.repository.UserRepository;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Slf4j
@ApplicationScoped
public class RegisterService {


    @ConfigProperty(name = "password.hashing.rounds", defaultValue = "10")
    Integer rounds;

    @Inject
    @RestClient
    SaltService saltService;

    @Inject
    UserRepository userRepository;

    @Inject
    RoutingContext context;

    @Transactional
    public Response save(UserDto dto) {

        AutoHashRes hashPassword = hashUserPassword(dto.getPassword());
        String userIp = getIpAddress();

        User user = User.builder()
                .username(dto.getUsername())
                .password(hashPassword.getPassword())
                .salt(hashPassword.getSalt())
                .ip(userIp)
                .build();

        try {
            log.debug("Attempting to save user to database: {}", user.getUsername());
            userRepository.persist(user);
            log.info("User successfully saved to database: {}", user.getUsername());
            return Response.status(Response.Status.CREATED).build();
        } catch (Exception e) {
            log.error("Failed to save user to database: {}", e.getMessage(), e);
            throw new RuntimeException("Database save operation failed.", e);
        }
    }

    // Kinda redundant if gateway will be implemented
    private String getIpAddress() {
        String ip;
        String xForwardedForHeader = context.request().getHeader("X-Forwarded-For");

        if (xForwardedForHeader != null) {
            ip = xForwardedForHeader.split(",")[0];
        } else {
            ip = context.request().remoteAddress().host();
        }

        log.debug("Retrieved IP address: {}", ip);
        return ip;
    }

    private AutoHashRes hashUserPassword(String password) {
        AutoHashReq req = new AutoHashReq(password, rounds);
        log.info("Hashing password for security.");
        try {
            AutoHashRes res = saltService.createSalt(req);
            log.debug("Hashed password: {}, Salt used: {}", res.getPassword(), res.getSalt());
            return res;
        } catch (Exception e) {
            log.error("Failed to hash password: {}", e.getMessage(), e);
            throw new RuntimeException("Password hashing failed.", e);
        }
    }
}
