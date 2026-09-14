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

애플리케이션은 PostgreSQL을 기본 데이터베이스로 사용하므로 별도 Spring 프로필 설정 없이 IDE에서 실행하면 된다. 기본 접속 주소와 계정은 `localhost:5432`, `exodusent` / `exodusent_dev`다. `.env`에서 DB 이름·계정·비밀번호를 바꿨다면 IDE 실행 설정에서 각각 `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD` 환경 변수도 맞춘다.

DB 컨테이너를 종료할 때는 다음 명령을 사용한다.

```sh
docker compose -f compose.db.yaml down
```

테스트도 PostgreSQL을 사용한다. 로컬에서 Gradle 테스트를 실행하기 전에 이 Compose 파일로 DB를 시작한다. GitHub Actions 테스트에는 별도의 PostgreSQL 서비스가 설정되어 있다. 기본 계정은 로컬 개발용이며 배포 환경에서는 반드시 별도의 비밀번호를 설정한다.
