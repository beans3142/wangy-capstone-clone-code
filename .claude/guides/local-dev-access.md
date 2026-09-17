# 🔐 로컬 개발 인프라 접속 정보 (Local Dev Access)

architecture.md 4.4가 참조하는 실제 접속 정보. 설계 결정이 아니라 "지금 이 컴퓨터에서 뭐가 어디 떠 있는지"라 별도 가이드로 뺐다. 값이 바뀌면 이 파일만 고치면 된다.

## PostgreSQL
* `jdbc:postgresql://${DATASOURCE_HOST:localhost}:${DATASOURCE_PORT:5433}/{서비스명}` — 계정 `postgres`/`root`.
* **포트 5433**(5432 아님) — 이 머신의 5432는 다른 프로젝트(`hwangjeon-db` 컨테이너)가 이미 씀, 절대 건드리지 않는다. 이 프로젝트 전용은 Docker 컨테이너 `wangyu-pg`.
* 운영 DB: `{서비스명}`(예: `user`), `ddl-auto: validate` + Flyway. 테스트 DB: `{서비스명}-test`, `ddl-auto: none`. 둘 다 이미 생성돼 있다.

## Kafka
* `spring.kafka.bootstrap-servers: ${KAFKA_HOST:localhost}:9092` — Docker 컨테이너 `wangyu-kafka`, 단일 노드. 토픽은 자동 생성되게 두고 수동 생성하지 않는다.

## MinIO (S3 호환)
* 엔드포인트 `http://localhost:9000`(환경변수 `S3_ENDPOINT`), 버킷 `wangyu-images`, 액세스키 `wangyu-dev` / 시크릿 `wangyu-dev-secret`(환경변수 `S3_ACCESS_KEY`/`S3_SECRET_KEY`).
* 실제 AWS 자격증명 없음 — `AmazonS3ClientBuilder.withEndpointConfiguration` + `withPathStyleAccessEnabled(true)`로 이 엔드포인트를 가리키면 AWS SDK 코드 그대로 붙는다.

## 단위 테스트는 이 인프라를 안 쓴다
`AbstractServiceTest`에서 Repository/Kafka Producer/Feign Client를 전부 `@MockBean`으로 대체하므로, 위 접속 정보는 애플리케이션을 실제로 기동하거나(`mvn spring-boot:run`) `local-e2e-be.md`/`local-e2e-fe.md` 같은 통합 검증을 할 때만 필요하다.
