# 🛑 커밋 전 훅 (Verification Pipeline)

에이전트는 작업한 코드를 커밋하기 직전에 아래의 파이프라인을 **순서대로** 통과해야 합니다. 실패 항목이 하나라도 있다면 커밋을 중단하고 즉시 코드를 수정하십시오.

## 1. 정적 분석 및 컴파일 검증
* **[MUST]** 백엔드의 경우 터미널에서 `mvn clean compile -pl {모듈명}`을 실행하여 문법 오류 확인.
* **[MUST]** 프론트엔드의 경우 `npm run typecheck` 및 `npm run lint` 실행.

## 2. 단위 테스트 검증
* **[MUST]** 터미널에서 단위 테스트(`mvn test` 또는 `npm run test`)가 Green 상태인지 확인.

## 3. 주석 및 클린 코드 점검 (clean-up-comments)
* **[MUST]** 프로젝트 클린 코드 정책에 따라, 코드 리뷰나 프롬프트 대화 맥락이 섞인 오염된 주석이 없는지 스스로 스캔하고 완벽히 삭제.

## 4. 적대적 셀프 리뷰 실행 (Adversarial Self-Review)
* **[MUST]** 3단계 주석 정리가 완료된 후, 코드를 `git add` 하고 **반드시 `adversarial-self-review-be`(백엔드) 또는 `adversarial-self-review-fe`(프론트엔드) 스킬을 실행**합니다. 대상 경로가 백엔드/프론트엔드 양쪽에 걸치면 둘 다 실행.
* 전체 파일이 아닌 `git diff`를 대상으로 4단계(임팩트 -> 스타일 -> 레이어 -> 종합) 검증 Loop를 돌며, 통과해야만 최종 커밋이 가능합니다.
