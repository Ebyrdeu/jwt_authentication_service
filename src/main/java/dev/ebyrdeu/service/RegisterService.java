package dev.ebyrdeu.service;

import dev.ebyrdeu.domain.dto.UserDto;
import dev.ebyrdeu.domain.hashing.AutoHashReq;
import dev.ebyrdeu.domain.hashing.AutoHashRes;
import dev.ebyrdeu.domain.entity.User;
import dev.ebyrdeu.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Slf4j
@ApplicationScoped
public class RegisterService {

    @Inject
    @RestClient
    SaltService saltService;

    @Inject
    UserRepository userRepository;


    @Transactional
    public Response save(UserDto dto) {
        AutoHashRes hashPassword = hashUserPassword(dto.getPassword());

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(hashPassword.getPassword());
        user.setSalt(hashPassword.getSalt());

        try {
            log.info("Saving user {}, {}, {} to database", user.getUsername(), user.getPassword(), user.getSalt());
            userRepository.persist(user);
            return Response.status(201).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    private AutoHashRes hashUserPassword(String password) {
        AutoHashReq req = new AutoHashReq(password, 10);
        try {
            log.info("---Hashing user password ---");
            AutoHashRes res = saltService.createSalt(req);
            log.debug(res.getPassword());
            log.debug(res.getSalt());
            return res;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
