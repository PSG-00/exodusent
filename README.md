# 🍜 짜장면 vs 짬뽕 투표 서비스 (Backend API)

짜장면과 짬뽕 중 선호하는 메뉴에 투표하고, 실시간 집계 결과를 조회할 수 있는 백엔드 REST API 서비스입니다.  
동시 다발적인 투표 요청 환경에서도 투표 데이터의 유실 및 중복 투표를 방지하도록 설계되었습니다.

---

## 🛠 기술 스택 (Tech Stack)

| 구분 | 기술 / 도구 | 상세 |
| :--- | :--- | :--- |
| **Language** | Java | Java 17 (Amazon Corretto / Temurin) |
| **Framework** | Spring Boot | Spring Boot Starter Web, Spring Data JPA, Validation |
| **Database** | PostgreSQL | PostgreSQL 17 (Docker Compose 연동) |
| **ORM** | Hibernate / JPA | Hibernate DDL Auto, 객체-관계 매핑 |
| **Build Tool** | Gradle | Gradle Wrapper |
| **API Docs** | Springdoc OpenAPI | Swagger UI (`/swagger-ui/index.html`) |
| **Container** | Docker / Compose | `compose.db.yaml`, `compose.yaml` |

---

## 📌 주요 기능 및 API 명세

### 1. 헬스 체크 (Health Check)
서버 및 서비스의 정상 동작 상태를 확인합니다.

- **Method / URL**: `GET /health`
- **Response**: `200 OK`
  ```text
  OK
  ```

---

### 2. 투표하기 (Vote)
사용자가 선호하는 메뉴(`jajang` 또는 `jjamppong`)에 투표합니다.

- **Method / URL**: `POST /api/vote`
- **Request Headers**: `Content-Type: application/json`
- **Request Body**:
  ```json
  {
    "choice": "jajang",
    "voterId": "user-123"
  }
  ```
  - `choice`: `"jajang"` 또는 `"jjamppong"` (필수, 외의 값은 검증 실패)
  - `voterId`: 투표자 고유 식별자 문자열 (필수)
- **Response**:
  - **성공 (`201 Created`)**:
    ```json
    {
      "id": 1,
      "choice": "jajang",
      "voterId": "user-123"
    }
    ```
  - **중복 투표 (`409 Conflict`)**:
    ```json
    {
      "status": 409,
      "code": "V001",
      "message": "이미 투표에 참여한 식별자입니다.",
      "timestamp": "2026-09-14T16:40:00.000"
    }
    ```
  - **입력값 누락 / 유효하지 않은 값 (`400 Bad Request`)**:
    ```json
    {
      "status": 400,
      "code": "CM01",
      "message": "적절하지 않은 요청 값입니다.",
      "fieldErrors": {
        "choice": "choice는 jajang 또는 jjamppong이어야 합니다."
      },
      "timestamp": "2026-09-14T16:40:00.000"
    }
    ```

---

### 3. 현재 투표 결과 조회 (Result)
실시간 투표 집계 현황을 확인합니다.

- **Method / URL**: `GET /api/result`
- **Response (`200 OK`)**:
  ```json
  {
    "jajang": 120,
    "jjamppong": 95,
    "total": 215
  }
  ```

---

## ⚡ 동시성 처리 및 데이터 정합성 설계

1. **DB 레벨의 Unique 제약조건 (`uk_votes_voter_id`)**
   - `votes` 테이블의 `voter_id` 컬럼에 Unique 제약조건을 설정하여, 동시 요청(Race Condition)으로 인한 애플리케이션 레벨의 선체크 우회를 완벽히 차단합니다.
2. **이중 방어 계층 (Two-tier Defense)**
   - 1차: `voteRepository.existsByVoterId(voterId)`를 통한 빠른 사전 검증
   - 2차: 동시성 트랜잭션 충돌 시 발생하는 `DataIntegrityViolationException`을 캐치하여 클라이언트에게 일관된 `409 Conflict`(`V001`) 표준 응답 반환
3. **트랜잭션 격리 및 조회 최적화**
   - 투표 생성 로직은 `@Transactional` 쓰기 트랜잭션으로 원자성을 보장합니다.
   - 결과 조회 로직은 `@Transactional(readOnly = true)`로 설정하여 불필요한 스냅샷 생성 및 Dirty Checking 비용을 절감합니다.

---

## 🚀 실행 가이드 (Getting Started)

### 1. PostgreSQL 데이터베이스 실행 (Docker)
PostgreSQL을 로컬 5432 포트로 실행합니다. Docker Volume(`exodusent_postgres_data`)을 통해 컨테이너가 재시작되어도 데이터가 안전하게 영속화됩니다.

```bash
# DB 컨테이너 실행
docker compose -f compose.db.yaml up -d
```

### 2. Spring Boot 애플리케이션 실행
PostgreSQL이 기동된 후 백엔드 애플리케이션을 실행합니다:

```bash
# Windows
.\gradlew.bat bootRun

# Linux / macOS
./gradlew bootRun
```

> **참고**: 애플리케이션 실행 시 JPA `ddl-auto: update` 설정에 따라 `votes` 테이블 및 유니크 제약조건이 자동으로 데이터베이스에 생성됩니다.

---

## 🧪 테스트 방법

### 1. 자동화 단위 테스트
```bash
# Windows
.\gradlew.bat test

# Linux / macOS
./gradlew test
```

### 2. cURL을 이용한 수동 테스트

```bash
# 1) 헬스체크
curl -X GET http://localhost:8080/health

# 2) 짜장면 투표
curl -X POST http://localhost:8080/api/vote \
  -H "Content-Type: application/json" \
  -d '{"choice": "jajang", "voterId": "user-123"}'

# 3) 중복 투표 시도 (409 에러 확인)
curl -X POST http://localhost:8080/api/vote \
  -H "Content-Type: application/json" \
  -d '{"choice": "jjamppong", "voterId": "user-123"}'

# 4) 집계 결과 조회
curl -X GET http://localhost:8080/api/result
```

### 3. HTTP 파일 테스트
IntelliJ HTTP Client 또는 VS Code REST Client를 사용하는 경우 프로젝트 루트의 [`test-requests.http`](file:///c:/Project/spring/exodusent/test-requests.http) 파일을 열어 각 요청을 바로 클릭하여 실행할 수 있습니다.
