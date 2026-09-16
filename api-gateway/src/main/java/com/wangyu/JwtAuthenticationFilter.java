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

import java.util.Optional;

/**
 * architecture.md 4.1의 인증 흐름을 완성한다: ①서명/만료 검증 → ②검증된 이메일로 user-service를
 * 조회 → ③X-Auth-User-Id 헤더를 주입해 하위 서비스로 전달. 활성화 여부(activationCode)는 Phase 3가
 * "정보성 필드로만 두고 로그인/인증을 막지 않는다"고 결정한 바 있어(phase03-agent-worklog.md 참고),
 * 여기서도 활성화 상태로 요청을 차단하지 않는다 — 존재하는 계정인지만 확인한다.
 */
@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private static final String AUTH_USER_ID_HEADER = "X-Auth-User-Id";

    private final JwtProvider jwtProvider;
    private final GatewayRouteProperties routeProperties;
    private final UserLookupClient userLookupClient;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public JwtAuthenticationFilter(JwtProvider jwtProvider,
                                    GatewayRouteProperties routeProperties,
                                    UserLookupClient userLookupClient) {
        this.jwtProvider = jwtProvider;
        this.routeProperties = routeProperties;
        this.userLookupClient = userLookupClient;
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

        String email = jwtProvider.resolveEmail(token);
        return userLookupClient.findByEmail(email)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMap(maybeUser -> maybeUser
                        .map(user -> chain.filter(withAuthUserIdHeader(exchange, user.getId())))
                        .orElseGet(() -> reject(exchange)));
    }

    private ServerWebExchange withAuthUserIdHeader(ServerWebExchange exchange, Long userId) {
        return exchange.mutate()
                .request(request -> request.headers(headers -> headers.set(AUTH_USER_ID_HEADER, String.valueOf(userId))))
                .build();
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
