# syntax=docker/dockerfile:1.7
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /workspace

# 의존성 메타데이터를 먼저 복사해 소스 변경 시 의존성 캐시를 재사용한다.
COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
RUN chmod +x ./gradlew
RUN --mount=type=cache,target=/root/.gradle ./gradlew --no-daemon dependencies --console=plain

COPY src ./src
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew --no-daemon bootJar -x test --console=plain \
    && artifact="$(find build/libs -maxdepth 1 -type f -name '*.jar' ! -name '*-plain.jar' -print -quit)" \
    && test -n "$artifact" \
    && cp "$artifact" /workspace/app.jar

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder --chown=10001:10001 /workspace/app.jar ./app.jar
USER 10001:10001
EXPOSE 8080
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]
