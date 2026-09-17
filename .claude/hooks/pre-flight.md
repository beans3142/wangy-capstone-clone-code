# 🛫 작업 전 훅 (Pre-Flight Context Hydration)

새로운 기능을 구현하라는 지시를 받은 즉시, 에이전트는 코드를 단 한 줄도 작성하기 전에 아래의 훅(Hook)을 반드시 실행해야 합니다.

## 1. 아키텍처 및 룰 동기화
* **[MUST]** `.claude/guides/feedback.md`를 먼저 읽어 과거 Phase에서 지적받았던 것들을 훑으십시오 — 같은 실수를 반복하지 않기 위함입니다.
* **[MUST]** `architecture.md` 전체를 읽어(Read) 프로젝트의 레이어 구조, Mapper 패턴, Redux Saga 규격을 파악하십시오.
* **[MUST]** `agents.md`를 읽어 절대 해서는 안 될 행동 강령을 숙지하십시오.
* **[MUST]** `.claude/tools.md`를 읽어 실제 사용 가능한 도구를 확인하십시오. 없는 도구를 임의로 설치하지 마십시오.

## 2. 작업 대상 스캔 (Reconnaissance)
* **[MUST]** 타겟 마이크로서비스의 `pom.xml` 또는 `package.json`을 읽어 현재 사용 가능한 라이브러리 버전을 파악하십시오. (임의의 버전을 추측하여 코딩하는 것을 방지).
* **[MUST]** 타겟 마이크로서비스의 `src/main/resources/application.yml`을 읽어 DB 연결 정보 및 활성화된 프로파일을 확인하십시오.
* **[MUST]** `commons` 모듈의 내부 구조를 스캔하여 `BasicMapper`, `HeaderResponse`, `GlobalExceptionHandler` 등의 정확한 패키지 경로를 파악하십시오.
