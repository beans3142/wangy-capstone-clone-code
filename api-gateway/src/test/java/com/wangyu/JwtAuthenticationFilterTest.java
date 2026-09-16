package com.wangyu;

import java.net.URI;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JwtAuthenticationFilterTest {

    private static final String SECRET = "test-only-secret-key-used-for-gateway-unit-tests-2026-abcdefg";

    private JwtAuthenticationFilter filter;
    private GatewayFilterChain chain;

    @Before
    public void setUp() {
        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setSecret(SECRET);

        GatewayRouteProperties routeProperties = new GatewayRouteProperties();
        routeProperties.setFreePaths(List.of("/api/user/login", "/api/user/signup"));

        filter = new JwtAuthenticationFilter(new JwtProvider(jwtProperties), routeProperties);
        chain = mock(GatewayFilterChain.class);
        when(chain.filter(any())).thenReturn(Mono.empty());
    }

    @Test
    public void passesThroughFreePathWithoutToken() {
        ServerWebExchange exchange = exchangeFor("/api/user/login", null);

        filter.filter(exchange, chain).block();

        verify(chain, times(1)).filter(exchange);
        verify(exchange.getResponse(), never()).setStatusCode(any());
    }

    @Test
    public void rejectsProtectedPathWithMissingAuthorizationHeader() {
        ServerWebExchange exchange = exchangeFor("/api/tweet/feed", null);

        filter.filter(exchange, chain).block();

        verify(chain, never()).filter(any());
        verify(exchange.getResponse()).setStatusCode(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void rejectsProtectedPathWithTamperedToken() {
        ServerWebExchange exchange = exchangeFor("/api/tweet/feed", "Bearer not-a-valid-jwt");

        filter.filter(exchange, chain).block();

        verify(chain, never()).filter(any());
        verify(exchange.getResponse()).setStatusCode(HttpStatus.UNAUTHORIZED);
    }

    private ServerWebExchange exchangeFor(String path, String authorizationHeaderValue) {
        HttpHeaders headers = new HttpHeaders();
        if (authorizationHeaderValue != null) {
            headers.add("Authorization", authorizationHeaderValue);
        }

        ServerHttpRequest request = mock(ServerHttpRequest.class);
        when(request.getURI()).thenReturn(URI.create("http://localhost" + path));
        when(request.getHeaders()).thenReturn(headers);

        ServerHttpResponse response = mock(ServerHttpResponse.class);
        when(response.setComplete()).thenReturn(Mono.empty());

        ServerWebExchange exchange = mock(ServerWebExchange.class);
        when(exchange.getRequest()).thenReturn(request);
        when(exchange.getResponse()).thenReturn(response);

        return exchange;
    }
}
