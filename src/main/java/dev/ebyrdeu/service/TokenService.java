package dev.ebyrdeu.service;

import dev.ebyrdeu.utils.TokenUtils;
import jakarta.enterprise.context.RequestScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.Claims;
import org.jose4j.jwt.JwtClaims;

import java.util.UUID;

@RequestScoped
public class TokenService {

    @ConfigProperty(name = "jwt.expiration.date", defaultValue = "3600")
    Float exp;

    public String generateToken(String subject, String username) {
        try {
            JwtClaims jwtClaims = new JwtClaims();
            jwtClaims.setIssuer("https://hello-world.com");
            jwtClaims.setSubject(subject);
            jwtClaims.setJwtId(UUID.randomUUID().toString());
            jwtClaims.setClaim(Claims.upn.name(), username);
            jwtClaims.setAudience("regular-user");
            jwtClaims.setExpirationTimeMinutesInTheFuture(exp);
            String token = TokenUtils.generateTokenString(jwtClaims);
            System.out.println(token);
            return token;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
