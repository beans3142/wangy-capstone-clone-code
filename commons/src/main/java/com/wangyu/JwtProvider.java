package com.wangyu;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

/**
 * 서명/만료 검증, 토큰 발급, 토큰에서 이메일(subject)을 꺼내는 것까지 담당한다.
 * 사용자 조회(활성화 여부 등)는 이 클래스의 책임이 아니다 — 그건 호출하는 쪽(게이트웨이, 또는
 * user-service가 생긴 이후의 다른 서비스)이 필요하면 별도로 한다.
 */
@Component
@EnableConfigurationProperties(JwtProperties.class)
public class JwtProvider {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final SecretKey signingKey;
    private final long expirationMillis;

    public JwtProvider(JwtProperties jwtProperties) {
        this.signingKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
        this.expirationMillis = jwtProperties.getExpirationMillis();
    }

    public String generateToken(String subject, Map<String, Object> claims) {
        Date now = new Date();
        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expirationMillis))
                .signWith(signingKey)
                .compact();
    }

    public String resolveToken(ServerHttpRequest request) {
        String header = request.getHeaders().getFirst(AUTHORIZATION_HEADER);
        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            return null;
        }
        return header.substring(BEARER_PREFIX.length());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }

    public String resolveEmail(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
