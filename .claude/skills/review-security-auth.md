---
name: review-security-auth
description: OWASP Secure Coding 기반 권한/보안 리뷰 스킬
version: 9.0.0
---

# 🧐 스킬: 보안 및 인증 리뷰 (OWASP Security Audit)

OWASP Code Review Guide를 기반으로 마이크로서비스 간 신뢰 경계(Trust Boundaries)를 검증합니다.

## 1. Review Scope
* 입력값 검증 (Input Validation) 및 인가(Authorization).
* JWT 토큰의 올바른 전파 및 컨텍스트 바인딩.

## 2. Reject Criteria (즉각 반려 기준)
1. Controller의 Request DTO 파라미터에 `@Valid` 어노테이션이 누락되어 있음.
2. DTO 내부 필드에 JSR-303 검증 어노테이션(`@NotBlank`, `@NotNull`, `@Size` 등)이 없음.
3. 타 마이크로서비스를 호출하는 `@FeignClient` 메서드가 인가 토큰을 넘기지 않거나, `commons`의 Interceptor를 우회하도록 설계됨.
