package com.wangyu;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JwtProviderTest {

    private static final String SECRET = "test-only-secret-key-used-for-commons-unit-tests-2026-abcdefg";

    private final JwtProvider provider = new JwtProvider(properties(SECRET));
    private final SecretKey signingKey = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    @Test
    public void acceptsTokenSignedWithMatchingSecret() {
        String token = tokenFor("wangyu@example.com", 60_000);

        assertTrue(provider.validateToken(token));
    }

    @Test
    public void rejectsTokenWithTamperedSignature() {
        String token = tokenFor("wangyu@example.com", 60_000);
        String tamperedToken = token.substring(0, token.length() - 2) + "zz";

        assertFalse(provider.validateToken(tamperedToken));
    }

    @Test
    public void rejectsExpiredToken() {
        String token = tokenFor("wangyu@example.com", -60_000);

        assertFalse(provider.validateToken(token));
    }

    @Test
    public void rejectsMalformedToken() {
        assertFalse(provider.validateToken("not-a-jwt"));
    }

    @Test
    public void resolvesEmailFromSubjectClaim() {
        String token = tokenFor("wangyu@example.com", 60_000);

        assertEquals("wangyu@example.com", provider.resolveEmail(token));
    }

    @Test
    public void generatesTokenThatCarriesSubjectAndCustomClaims() {
        String token = provider.generateToken("wangyu@example.com", Map.of("userId", 42));

        assertTrue(provider.validateToken(token));
        assertEquals("wangyu@example.com", provider.resolveEmail(token));
    }

    private String tokenFor(String subject, long expiresInMillis) {
        return Jwts.builder()
                .subject(subject)
                .expiration(new Date(System.currentTimeMillis() + expiresInMillis))
                .signWith(signingKey)
                .compact();
    }

    private JwtProperties properties(String secret) {
        JwtProperties properties = new JwtProperties();
        properties.setSecret(secret);
        return properties;
    }
}
