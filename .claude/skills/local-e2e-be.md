---
name: local-e2e-be
description: 실제로 서비스들을 로컬에 띄운 뒤 curl로 핵심 시나리오(회원가입→로그인→트윗→좋아요→팔로우)를 끝까지 통과시켜본다. 단위 테스트(Mock)와 달리 진짜 요청/응답을 확인한다.
version: 1.0.0
---

# 🔌 스킬: 로컬 E2E 백엔드 검증 (curl 기반)

`mvn test`는 Repository/Feign을 `@MockBean`으로 대체한 단위 테스트다 — 서비스들이 실제로 서로 통신 가능한지는 검증하지 않는다. 이 스킬은 여러 서비스를 한 Phase에 걸쳐 통합할 때(또는 릴리스 전) 실제로 떠 있는 상태에서 curl로 확인한다.

## 1. 기동 순서 (MUST)
1. 로컬 인프라(Postgres/Kafka/MinIO)가 떠 있는지 확인: `docker ps`로 `wangyu-pg`, `wangyu-kafka`, `wangyu-minio` 확인.
2. `eureka-server` → `config-server` 순으로 기동, `http://localhost:8761`에서 등록 상태 확인.
3. 나머지 서비스(`user-service`, `email-service`, `image-service`, `tweet-service`, `api-gateway`)는 각자 `mvn spring-boot:run -pl {모듈}` (백그라운드) 또는 빌드된 jar로 기동.
4. 모든 서비스가 Eureka에 `UP`으로 뜬 뒤에만 다음 단계로 진행한다(`http://localhost:8761/eureka/apps`로 확인).

## 2. 핵심 시나리오 curl 스크립트 (MUST, 게이트웨이 경유 — `http://localhost:8080`)
```bash
# 1. 회원가입
curl -s -X POST http://localhost:8080/api/v1/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"email":"e2e@example.com","password":"Passw0rd!","nickname":"e2e"}'

# 2. 로그인 → JWT 추출
TOKEN=$(curl -s -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"e2e@example.com","password":"Passw0rd!"}' | jq -r '.token')

# 3. 트윗 작성 (인증 필요)
TWEET_ID=$(curl -s -X POST http://localhost:8080/api/v1/tweets \
  -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"content":"E2E 테스트 트윗"}' | jq -r '.id')

# 4. 좋아요 토글
curl -s -X POST http://localhost:8080/api/v1/tweets/$TWEET_ID/like \
  -H "Authorization: Bearer $TOKEN"

# 5. 타임라인 조회 (헤더 페이징 메타 확인)
curl -sD - http://localhost:8080/api/v1/users/1/timeline?page=0&size=10 \
  -H "Authorization: Bearer $TOKEN" -o /dev/null
```

## 3. 판정 기준 (MUST)
* 모든 단계가 2xx를 반환해야 한다. 401/403/5xx가 나오면 어느 단계에서 실패했는지 로그(각 서비스 콘솔)를 대조해 원인을 찾는다.
* 3~5단계는 게이트웨이가 주입한 `X-Auth-User-Id` 헤더로 인증되므로, JWT 없이 호출 시 401이 나와야 정상이다(음성 테스트도 1회 포함).
* 타임라인 응답 헤더에 `X-Total-Count`/`X-Has-Next`가 존재하는지 확인한다(`-D -`로 헤더 출력).

## 4. 종료
테스트 후 기동한 프로세스를 전부 종료한다. 인프라 컨테이너(Postgres/Kafka/MinIO)는 다른 작업에서도 계속 쓰이니 내리지 않는다.
