# Docker 개발 환경

Compose 파일은 로컬 개발용 기본 PostgreSQL 계정(`exodusent` / `exodusent_dev`)을 사용한다. 다른 값을 쓰려면 `.env.example`을 `.env`로 복사하고 값을 바꾼다. `.env`는 Git에 커밋하지 않는다.

## 앱과 PostgreSQL 함께 실행

저장소 루트에서 실행한다.

```sh
docker compose up --build
```

앱은 `http://localhost:8080`에서, PostgreSQL은 `localhost:5432`에서 접근한다. DB가 healthy 상태가 된 뒤 앱이 시작된다. 종료할 때 `docker compose down`을 실행한다. 데이터 볼륨은 유지되며, DB 데이터를 지울 때만 `docker compose down -v`를 사용한다.

## IDE에서 앱을 실행하고 PostgreSQL만 Compose로 실행

```sh
docker compose -f compose.db.yaml up -d
```

IDE 실행 설정에 `SPRING_PROFILES_ACTIVE=postgres` 환경 변수를 지정한다. 기본 PostgreSQL 주소와 계정은 `localhost:5432`, `exodusent` / `exodusent_dev`다. `.env`에서 DB 계정이나 비밀번호를 바꿨다면 IDE 실행 설정의 `SPRING_DATASOURCE_USERNAME`과 `SPRING_DATASOURCE_PASSWORD`도 같은 값으로 맞춘다.

DB 컨테이너를 종료할 때는 다음 명령을 사용한다.

```sh
docker compose -f compose.db.yaml down
```

기본 H2 설정은 그대로 두므로 `postgres` 프로필을 활성화하지 않은 테스트와 기존 실행은 H2를 사용한다. 기본 계정은 로컬 개발용이며 배포 환경에서는 반드시 별도의 비밀번호를 설정한다.
