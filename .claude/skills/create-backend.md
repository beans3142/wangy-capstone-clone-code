---
name: create-backend
description: 백엔드 마이크로서비스 기능 구현을 위한 표준 운영 절차 (SOP)
version: 9.0.0
---

# ⚙️ 스킬: 백엔드 기능 구현 SOP (Standard Operating Procedure)

본 문서는 AI 에이전트가 백엔드 API를 구현할 때 준수해야 하는 Deterministic(결정론적) 실행 파이프라인입니다.

## 1. Objective (목표)
* 요구사항에 맞는 Controller, Mapper, Service, Repository, DTO를 구현하고 모든 단위 테스트를 통과시킨다.

## 2. Pre-conditions (사전 조건)
* 에이전트는 작업 시작 전 `architecture.md`의 **"3. 백엔드 설계 및 코드 제약"**을 완벽히 숙지해야 한다.

## 3. Execution Pipeline (실행 단계)
**Step 1: 인터페이스 및 DTO 설계**
* Request/Response DTO를 작성하고 `jakarta.validation` 어노테이션(`@NotBlank` 등)을 추가한다.
* `Repository`에 반환할 `Projection` 인터페이스를 설계한다.

**Step 2: 실패하는 테스트 작성 (Red)**
* `AbstractServiceTest`를 상속받는 단위 테스트를 작성한다. (`org.junit.Test` 사용)
* 의존성은 `@MockBean`을 활용하여 모킹하고, 로직을 검증한다.
* 터미널에서 `mvn test -pl {해당_모듈}`을 실행하여 테스트가 실패하는지(Red) 확인한다.

**Step 3: 프로덕션 코드 구현 (Implementation)**
* 동적 프로젝션(`Class<T> type`)을 사용하는 JPQL 기반 Repository 메서드를 구현한다.
* `BasicMapper`를 사용하여 Controller 계층에 `HeaderResponse`를 반환하도록 구현한다.

**Step 4: 검증 및 픽스 (Green)**
* 터미널에서 `mvn test -pl {해당_모듈}`을 다시 실행한다.
* **Fallback:** 만약 실패한다면 코드를 전면 재작성하지 말고, 스택 트레이스를 분석하여 실패한 지점만 수정(Isolate fix)한다.

## 4. Constraints (제약 조건)
* Native Query 사용 금지.
* `MapStruct`, JUnit 5 `@ExtendWith` 사용 금지.
