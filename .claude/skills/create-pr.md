---
name: create-pr
description: 검증 루프를 통과한 코드를 PR(Pull Request)로 생성하는 스킬
version: 1.0.0
---

# 🚀 스킬: Pull Request 생성 (PR Create)

본 스킬은 `adversarial-self-review` 루프를 완벽히 통과한 코드만을 PR로 올리기 위한 파이프라인입니다.

## 1. Pre-conditions
* `adversarial-self-review-be` 또는 `adversarial-self-review-fe`를 통과했음을 터미널 출력이나 로그로 증명할 수 있어야 합니다.

## 2. Branch & PR Pipeline
**Step 1: 브랜치 전략 확인**
* 현재 브랜치가 `feature/phase{번호}-{도메인명}` 형식인지 확인합니다. (예: `feature/phase03-user-auth`).
* 만약 `main` 브랜치에서 직접 작업했다면, 즉시 `git checkout -b`로 브랜치를 분리하십시오.

**Step 2: 커밋 규칙 검증**
* `git log -1`을 확인하여 커밋 메시지가 `feat(user): add login logic` 형태의 Conventional Commits를 따르는지 확인합니다.

**Step 3: PR 템플릿 작성 및 생성**
* 다음 마크다운 템플릿에 맞추어 PR 본문을 작성하고, 터미널에서 `gh pr create` 명령어(또는 유사한 git 도구)를 통해 PR을 생성합니다.

```markdown
## 🎯 구현 목표 (Objective)
- (예: 유저 로그인 및 JWT 발급 기능 구현)

## 🛡️ 적대적 리뷰 (Adversarial Review) 통과 내역
- [x] JUnit 4 및 AbstractServiceTest 상속 확인 완료.
- [x] UserRepository 동적 프로젝션(Class<T> type) 적용 완료.
- [x] mvn test (또는 npm run typecheck) 100% Green 확인 완료.

## 💡 주요 아키텍처 결정 사항
- (어떤 구조적 선택을 했는지 간략히 기재)
```
