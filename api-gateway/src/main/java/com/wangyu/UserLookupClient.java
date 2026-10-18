package com.wangyu;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

/**
 * architecture.md 4.1의 ②단계 — 서명 검증을 통과한 토큰의 이메일로 user-service를 조회해
 * 계정 식별자를 얻는다. Eureka에 등록된 이름("user-service")으로 lb:// 스킴을 통해 부르므로
 * 실제 IP/포트를 하드코딩하지 않는다.
 */
@Component
public class UserLookupClient {

    private final WebClient webClient;

    public UserLookupClient(WebClient.Builder loadBalancedWebClientBuilder) {
        this.webClient = loadBalancedWebClientBuilder.baseUrl("lb://user-service").build();
    }

    public Mono<AuthUserResponse> findByEmail(String email) {
        return webClient.get()
                .uri("/api/v1/auth/user/{email}", email)
                .retrieve()
                .bodyToMono(AuthUserResponse.class)
                .onErrorResume(WebClientResponseException.class, exception -> Mono.empty());
    }
}
