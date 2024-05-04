package dev.ebyrdeu.utils;

import org.jboss.logging.Logger;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.lang.JoseException;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;

public class TokenUtils {
    private static final Logger LOGGER = Logger.getLogger(TokenUtils.class.getName());

    private TokenUtils() {
    }

    /**
     * Generates a JSON Web Token (JWT) as a string from the given JWT claims using the default private key.
     *
     * @param claims the {@link JwtClaims} object containing all the claims to be included in the JWT.
     * @return a {@link String} JWT.
     * @throws JoseException if there is an error in creating the JWT.
     */
    public static String generateTokenString(JwtClaims claims) throws JoseException {
        PrivateKey pk = readPrivateKey("/privateKey.pem");
        return generateTokenString(pk, claims);
    }

    /**
     * Generates a JSON Web Token (JWT) as a string using the provided private key and key ID.
     *
     * @param privateKey the {@link PrivateKey} to sign the JWT.
     * @param claims     the {@link JwtClaims} to be included in the JWT.
     * @return a compact URL-safe JWT string.
     * @throws JoseException if there is an error in processing the JWT.
     */
    private static String generateTokenString(PrivateKey privateKey, JwtClaims claims) throws JoseException {
        long currentTimeInSecs = currentTimeInSecs();
        claims.setIssuedAt(NumericDate.fromSeconds(currentTimeInSecs));

        logClaims(claims);

        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKey(privateKey);
        jws.setKeyIdHeaderValue("/privateKey.pem");
        jws.setHeader("typ", "JWT");
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

        return jws.getCompactSerialization();
    }

    /**
     * Logs each claim added to the JWT.
     *
     * @param claims the {@link JwtClaims} containing all the claims.
     */
    private static void logClaims(JwtClaims claims) {
        for (Map.Entry<String, Object> entry : claims.getClaimsMap().entrySet()) {
            LOGGER.info(String.format("Added claim: %s, value: %s", entry.getKey(), entry.getValue()));
        }
    }

    /**
     * Reads a PEM encoded private key from the classpath.
     *
     * @param pemResName the resource name where the PEM file is located.
     * @return a {@link PrivateKey} based on the provided PEM data.
     * @throws JoseException if there is an error reading or decoding the private key.
     */
    public static PrivateKey readPrivateKey(final String pemResName) throws JoseException {
        try (InputStream contentIS = TokenUtils.class.getResourceAsStream(pemResName)) {
            byte[] tmp = new byte[Objects.requireNonNull(contentIS).available()];
            contentIS.read(tmp);
            return decodePrivateKey(new String(tmp, StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new JoseException("Failed to read private key", e);
        }
    }

    /**
     * Decodes a PEM encoded private key string to a {@link PrivateKey}.
     *
     * @param pemEncoded the PEM encoded private key as a string.
     * @return a {@link PrivateKey} object.
     * @throws JoseException if decoding the key fails.
     */
    private static PrivateKey decodePrivateKey(final String pemEncoded) throws JoseException {
        try {
            byte[] encodedBytes = toEncodedBytes(pemEncoded);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new JoseException("Failed to decode private key", e);
        }
    }

    /**
     * Converts a PEM encoded private key string to a byte array after stripping headers and whitespaces.
     *
     * @param pemEncoded the PEM encoded private key string.
     * @return a byte array of the decoded key.
     */
    private static byte[] toEncodedBytes(final String pemEncoded) {
        String normalizedPem = pemEncoded
                .replaceAll("-----BEGIN (.*)-----", "")
                .replaceAll("-----END (.*)-----", "")
                .replaceAll("\\s+", "");
        return Base64.getDecoder().decode(normalizedPem);
    }

    /**
     * Returns the current time in seconds since epoch.
     *
     * @return the current time in seconds.
     */
    private static int currentTimeInSecs() {
        return (int) (System.currentTimeMillis() / 1000);
    }
}
