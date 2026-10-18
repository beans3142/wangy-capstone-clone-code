# 📋 누적 피드백 (지적받은 것 → 고친 것)

과거 Phase에서 리뷰(Gemini 적대적 리뷰 또는 마스터 점검)로 지적받았던 것들의 요약. architecture.md를 전부 다시 읽기 전에, 여기부터 훑으면 같은 실수를 반복할 확률이 줄어든다. 각 항목은 원인이 된 규칙 공백이 이미 architecture.md/스킬에 반영되어 있다 — 여기는 "왜 그 규칙이 생겼는지"의 빠른 색인이다.

| Phase | 지적받은 것 | 원인 | 지금 반영된 곳 |
|---|---|---|---|
| 1 | groupId를 팀이 임의로(`com.wangy`) 지음, 서비스명이 최상위 패키지에 접미사로 붙음(`com.wangyu.eurekaserver`) | groupId/패키지 컨벤션이 문서에 없었음 | architecture.md 4.3 |
| 1 | 호스트를 `localhost`로 하드코딩, Zipkin 설정 누락, config-server가 자기 자신을 Eureka에 등록 안 함 | 컨테이너 배포·분산추적·서비스디스커버리 컨벤션이 문서에 없었음 | architecture.md 4.3 |
| 1 | 원본에 없는 `enable-self-preservation: false` 같은 임의 튜닝 플래그 추가 | "명시 안 된 설정은 손대지 마라"는 원칙이 없었음 | architecture.md 4.3 |
| 2 | 예외 응답을 구조화 JSON({status,message,timestamp})으로 감쌈 | architecture.md 자체가 "공통 JSON 스펙"이라고 잘못 서술(실제 원본은 단순 문자열) | architecture.md 4.2 정정 |
| 2 | WebFlux(api-gateway)가 commons를 의존하며 서블릿 전용 빈(FeignRequestInterceptor)까지 끌고 와 `NoClassDefFoundError` | 여러 모듈이 flat 패키지를 공유하면 컴포넌트 스캔이 의도치 않게 번짐 | `@ConditionalOnClass` 패턴, architecture.md 4.3 "flat" 범위 명확화 |
| — | check-test-coverage 스킬이 2 Phase 연속 미실행 | jacoco 플러그인 자체가 없었고, pre-commit 훅에 트리거 연결이 없었음 | 루트 pom jacoco 플러그인 + `.claude/hooks/pre-commit.md` 명시 연결 |
| 3→5 | Phase 5(타임라인)가 팔로우 데이터를 요구하는데 Phase 3 PRD엔 없었음 | 로드맵 설계 시점에 기능 간 의존성을 놓침 | Phase 3b로 보정, PRD에 상호 의존성 명시 습관화 |
| 6 | (설계 질문) 좋아요 100명 동시 클릭 시 카운트 정확성 | 비정규화 카운터를 읽고-계산해서-쓰면 동시 요청에 유실됨 | `@Modifying` 원자적 UPDATE(`count = count + 1`) 패턴 채택 |
| 사후 | 게이트웨이가 서명 검증만 하고 user-service 조회·헤더 주입(architecture.md 4.1 ②③)을 안 함 | Phase 2 시점엔 user-service가 없어 미룬 상태로 계속 남아있었음 | Phase 5 종료 시점에 마스터가 직접 연결, `UserLookupClient` |

## 아직 안 고친 것 (알고 있는 미해결 항목)
* `.claude/skills/review-*.md` 4종의 실행 트리거가 여전히 암묵적(서브 에이전트가 파일명만 보고 스스로 판단) — 명시적 트리거 조건을 아직 안 정함.
* 로컬 E2E(실제로 서비스 다 띄우고 curl/Playwright로 검증)는 스킬만 만들었고 전체 서비스 대상으로 실제 실행해본 적은 아직 없음.
