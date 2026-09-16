---
name: review-architecture
description: Google Code Review 및 시스템 아키텍처 정합성 검증 스킬
version: 9.0.0
---

# 🧐 스킬: 아키텍처 리뷰 (Code Health & Architecture)

Google Code Review Best Practices를 기반으로 PR의 아키텍처 정합성을 평가합니다.

## 1. Review Scope
* 해당 코드가 `architecture.md`에 명시된 레이어 책임(Controller -> Mapper -> Service -> Repository)을 준수하는가?
* 의존성 방향이 올바르며 순환 참조가 없는가?

## 2. Reject Criteria (즉각 반려 기준)
에이전트는 다음 항목 중 하나라도 발견되면 즉시 코드를 반려(Reject)해야 합니다.
1. Controller가 Service를 직접 주입받음 (Mapper 누락).
2. 타 마이크로서비스의 Entity나 Repository를 직접 import 함.
3. Native SQL Query(`nativeQuery=true`)가 하나라도 존재함.
4. JPA 반환형이 동적 프로젝션(`Class<T> type`)이 아닌 원본 Entity 형태임.
5. 프론트엔드 컴포넌트 안에 `useSelector`나 `useDispatch`가 직접 하드코딩되어 있음 (Custom Hook 패턴 위반).
