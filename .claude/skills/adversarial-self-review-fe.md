---
name: adversarial-self-review-fe
description: 프론트엔드 커밋 전 diff(변경분)만을 4단계로 순차 검토한다. 훅이 clean-up-comments 다음에 강제 실행한다.
version: 2.0.0
---

# 🧐 스킬: 프론트엔드 적대적 셀프 리뷰 (Adversarial Self-Review - FE)

**전체 파일이 아니라 `git diff`(staged, 마지막 커밋 대비)만 본다.** 4단계를 순서대로, 각 단계는 앞 단계 결과를 넘겨받아 진행한다.

각 단계에서 실패(위반 사항 발견) 시 성공할 때까지 반드시 코드 수정 후 해당 단계부터 다시 Loop를 돌며, 성공해야만 다음 단계로 넘어간다.

## 1단계 — 임팩트 조사 + 버그/취약점
- 이 `git diff`가 바꾸는 커스텀 훅이나 상태(Redux)가 다른 컴포넌트에 사이드 이펙트를 유발하는지 추적.
- 버그: 무한 렌더링 루프(useEffect 의존성 배열), null/undefined 참조 처리.
- 취약점: XSS 방어를 우회하는 `dangerouslySetInnerHTML` 불필요한 사용.

## 2단계 — 코드 스타일 / 네이밍 / 재사용성
- `architecture.md` 기준으로 네이밍 대조 (Custom Hook은 `use{Name}.ts` 형태인지).
- 스타일링: `makeStyles` (MUI v4)를 정확히 사용했는지, `styled-components` 등을 무단으로 섞어 썼는지 점검.
- 이미 있는 훅이나 유틸리티를 새로 복제해서 짠 부분(재사용 실패).
- file → method → line 단위로 훑는다 — 파일 훑고 끝내지 않는다.

## 3단계 — 레이어 책임 준수 / 코드 흐름 / 효율성
- `architecture.md`의 계층 규칙을 벗어난 곳 (예: View 컴포넌트에 `useSelector`, `useDispatch` 등의 비즈니스 로직이 침범했는지).
- 비동기 로직 책임: 컴포넌트 내부나 Thunk에서 API를 직접 호출했는지 점검 (반드시 Redux Saga 제너레이터 함수로 분리).
- 코드 흐름이 자연스러운지 (불필요한 리렌더링 유발 등).

## 4단계 — 종합조사 및 검토
- `diff` 에 대해 1~3단계의 맥락을 모아 전체적 종합 검토 수행.
- 터미널 분석(`npm run typecheck` 및 `lint`) 통과 여부 최종 점검.
