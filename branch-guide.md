# 🌿 브랜치 및 형상 관리 가이드 (Branch Guide)

트위터 클론 프로젝트의 안정적인 CI/CD 및 코드 리뷰를 위해 아래의 브랜치 전략을 강제합니다. 에이전트는 절대 `main` 브랜치에 직접 코드를 푸시할 수 없습니다.

## 1. 브랜치 네이밍 컨벤션 (Branch Naming Convention)
* **포맷:** `feature/phase{로드맵번호}-{도메인/기능명}`
* **예시:** 
  * Phase 3의 유저 서비스 작업 시 -> `feature/phase03-user-service`
  * Phase 6의 프론트엔드 작업 시 -> `feature/phase06-frontend-interaction`

## 2. 작업 워크플로우 (Git Workflow)
1. **Branching:** 작업을 시작하기 전 반드시 `main` 브랜치 최신본에서 `git checkout -b`를 통해 브랜치를 생성합니다.
2. **Commit:** 변경 사항은 논리적인 단위로 쪼개어 커밋하며, 반드시 **Conventional Commits** (`feat:`, `fix:`, `refactor:`, `test:`) 규약을 따릅니다.
3. **Adversarial Review:** 코딩이 끝나면 PR을 올리기 전 `adversarial-self-review-be`/`adversarial-self-review-fe` 스킬을 실행하여 자체 적대적 검증 루프를 통과해야 합니다.
4. **Pull Request:** 검증 루프를 통과하면 `create-pr` 스킬을 사용하여 PR을 생성합니다.
