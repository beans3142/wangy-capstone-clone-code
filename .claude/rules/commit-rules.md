# 📌 Git 커밋 컨벤션 (강제 규약)

에이전트가 코드를 커밋할 때 메시지는 아래 규칙을 100% 준수해야 합니다. 

## 1. 커밋 포맷 (Conventional Commits)
`type(scope): subject`

* **예시:** `feat(user): add login logic`, `fix(tweet): correct like count race condition`
* **허용되는 type:**
  * `feat` (기능 추가), `fix` (버그 수정), `refactor` (리팩토링), `style` (포맷팅), `docs` (문서), `test` (테스트 코드), `chore` (빌드/설정)
* **scope:** 대상 마이크로서비스 또는 도메인명 (예: `user`, `tweet`, `gateway`)

## 2. 작성 금지 사항 (MUST NOT)
* **[MUST NOT]** "내가 무엇을 했는지"를 설명하는 긴 소설을 쓰지 마십시오. (예: `Feat: Controller를 만들고 Mapper를 연결하여 로직을 짬` -> 거절됨). 변경 사항은 diff가 설명하므로, 커밋 메시지는 **"왜(Why)"** 이 변경이 필요했는지에 집중하십시오.
* **[MUST NOT]** 커밋 메시지에 에이전트 자신을 지칭하거나 인간 사용자와의 대화 맥락을 넣지 마십시오. (예: `Fix: 사용자 요청에 따라 DTO 필드 수정` -> 거절됨).
* **[MUST NOT]** Subject 라인은 50자를 초과할 수 없으며 마지막에 마침표(.)를 찍지 않습니다.
