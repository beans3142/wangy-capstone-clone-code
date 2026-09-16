---
name: review-jpa-performance
description: [Master Agent] JPA 성능, N+1, DB 메모리 누수 리뷰
version: 7.0.0
---

# 🕵️ 스킬: DB 및 JPA 성능 리뷰

데이터베이스 과부하를 방지하기 위한 정밀 리뷰입니다.

## 🚨 거절(Reject) 기준
1. **N+1 쿼리 유발:** `for` 루프 내부에서 연관된 엔티티를 조회하여 쿼리가 반복 실행되는 안티패턴이 존재하는 경우.
2. **EAGER 로딩 검출:** `@ManyToOne` 또는 `@OneToOne` 연관관계 선언 시 `fetch = FetchType.LAZY` 속성이 누락된 경우.
3. **무한 재귀 렌더링 위험:** `@Entity` 클래스 레벨에 Lombok의 `@Data`, `@ToString`, `@EqualsAndHashCode` 어노테이션이 하나라도 선언된 경우.
4. **트랜잭션 누락:** 데이터 변경이 일어나는 `Service` 메서드에 `@Transactional`이 누락된 경우.
