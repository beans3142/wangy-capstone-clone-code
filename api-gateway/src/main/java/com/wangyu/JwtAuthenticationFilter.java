package com.wangyu;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 서명/만료 검증까지만 게이트웨이에서 수행한다. 검증된 이메일로 user-service를 조회해
 * 활성화 여부를 확인하고 X-Auth-User-Id 헤더를 주입하는 것(원본 AuthFilter의 나머지 절반)은
 * user-service(Phase 3)가 존재해야 실제로 의미가 있어, 이번 Phase에서는 구현하지 않는다.
 * architecture.md 4.1 참고.
 */
@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtProvider jwtProvider;
    private final GatewayRouteProperties routeProperties;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public JwtAuthenticationFilter(JwtProvider jwtProvider, GatewayRouteProperties routeProperties) {
        this.jwtProvider = jwtProvider;
        this.routeProperties = routeProperties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (isFreePath(path)) {
            return chain.filter(exchange);
        }

        String token = jwtProvider.resolveToken(exchange.getRequest());
        if (token == null || !jwtProvider.validateToken(token)) {
            return reject(exchange);
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    private boolean isFreePath(String path) {
        return routeProperties.getFreePaths().stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    private Mono<Void> reject(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }
}
