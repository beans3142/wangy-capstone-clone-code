package com.wangyu;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JwtValidatorTest {

    private static final String SECRET = "test-only-secret-key-used-for-gateway-unit-tests-2026-abcdefg";

    private final JwtProperties properties = properties(SECRET);
    private final JwtValidator validator = new JwtValidator(properties);
    private final SecretKey signingKey = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    @Test
    public void acceptsTokenSignedWithMatchingSecret() {
        String token = Jwts.builder()
                .subject("wangyu")
                .expiration(new Date(System.currentTimeMillis() + 60_000))
                .signWith(signingKey)
                .compact();

        assertTrue(validator.isValid(token));
    }

    @Test
    public void rejectsTokenWithTamperedSignature() {
        String token = Jwts.builder()
                .subject("wangyu")
                .expiration(new Date(System.currentTimeMillis() + 60_000))
                .signWith(signingKey)
                .compact();
        String tamperedToken = token.substring(0, token.length() - 2) + "zz";

        assertFalse(validator.isValid(tamperedToken));
    }

    @Test
    public void rejectsExpiredToken() {
        String token = Jwts.builder()
                .subject("wangyu")
                .expiration(new Date(System.currentTimeMillis() - 60_000))
                .signWith(signingKey)
                .compact();

        assertFalse(validator.isValid(token));
    }

    @Test
    public void rejectsMalformedToken() {
        assertFalse(validator.isValid("not-a-jwt"));
    }

    private JwtProperties properties(String secret) {
        JwtProperties properties = new JwtProperties();
        properties.setSecret(secret);
        return properties;
    }
}
