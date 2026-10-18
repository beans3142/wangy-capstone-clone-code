# 🧰 사용 가능한 도구 목록 (Tools)

이 프로젝트에서 에이전트(서브 에이전트/마스터 모두)가 실제로 쓸 수 있는 도구다. 여기 없는 도구를 임의로 설치하려 하지 말고, 필요하면 먼저 요청하라.

## 빌드 / 언어
* `mvn` — Maven 3.9.16 (Java 21/25). 루트에서 `mvn -pl {모듈} -am {goal}`로 특정 모듈만 빌드.
* `npm` — Node.js/npm (frontend 모듈 전용).

## 버전관리 / 리뷰
* `git`, `gh` — 브랜치·커밋·PR. `gh` 인증 완료, `origin` 리모트 연결됨.
* `agy` (antigravity cli) — Gemini 모델 호출용. 마스터 전용, 서브 에이전트는 직접 쓰지 않는다 (`skills/gemini-adversarial-review.md` 참고, harness-orchestration에 있음).

## 로컬 인프라 (Docker, 이미 떠 있음 — architecture.md 4.4)
* PostgreSQL(`wangyu-pg`, 포트 5433), Kafka(`wangyu-kafka`, 포트 9092), MinIO(`wangyu-minio`, S3 호환, 포트 9000).
* `docker`, `docker exec` — 컨테이너 상태 확인/psql 접속 등에 한정. 새 컨테이너를 임의로 띄우거나 기존 컨테이너를 내리지 마라(다른 세션이 쓰고 있을 수 있음, 특히 `hwangjeon-db`는 이 프로젝트 것이 아니니 절대 건드리지 않는다).

## 로컬 E2E / QA
* `curl` — 백엔드 스모크 테스트(`skills/local-e2e-be.md`).
* `npx playwright` — 프론트엔드 E2E 테스트(`skills/local-e2e-fe.md`). frontend 모듈에 devDependency로 추가되어 있어야 한다.

## 하지 않는 것
* 실제 AWS/클라우드 자격증명을 요구하는 도구(S3/RDS 등) — 전부 로컬 대체재(MinIO/Postgres)로 대신한다.
* `harness-goal`, `harness-orchestration/legacy` 접근 — 블라인드 클론코딩 대상 서브 에이전트는 절대 금지.
