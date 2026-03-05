# 1단계: 빌드 스테이지
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

# 빌드에 필요한 파일 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

# [수정] 의존성 다운로드 시 TLS 설정 및 타임아웃 추가
RUN chmod +x ./gradlew
RUN ./gradlew dependencies --no-daemon \
    -Dhttps.protocols=TLSv1.2,TLSv1.3 \
    -Dsun.security.ssl.allowUnsafeRenegotiation=true \
    -Dorg.gradle.internal.http.socketTimeout=120000 \
    -Dorg.gradle.internal.http.connectionTimeout=120000

# 소스 코드 복사 및 빌드
COPY src src

# [수정] 빌드 시에도 동일한 보안 옵션 적용 + 테스트 제외(-x test)로 속도 향상
RUN ./gradlew clean bootJar --no-daemon \
    -x test \
    -Dhttps.protocols=TLSv1.2,TLSv1.3 \
    -Dsun.security.ssl.allowUnsafeRenegotiation=true

# 2단계: 실행 스테이지
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# 빌드 스테이지에서 생성된 jar 파일만 가져옴
COPY --from=build /app/build/libs/*.jar app.jar

# Spring Boot 기본 포트 개방
EXPOSE 8080

# 앱 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
