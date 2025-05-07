# 1️⃣ Build Stage: 애플리케이션을 컴파일하고 빌드 결과(JAR)를 생성합니다.
FROM eclipse-temurin:17 AS builder

# 작업 디렉토리 설정
WORKDIR /app

# Gradle Wrapper 관련 파일만 먼저 복사 → 의존성 캐싱
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle settings.gradle ./

# gradlew 권한 부여
RUN chmod +x ./gradlew

# 의존성만 미리 다운로드 (다음 레이어에서 소스가 변경되어도 다시 받지 않도록)
RUN ./gradlew dependencies --no-daemon

# 전체 프로젝트 소스 복사
COPY . .

# 실제 빌드 (테스트 코드를 제외하여 속도 최적화)
RUN ./gradlew build --no-daemon --parallel --build-cache -x test

# 2️⃣ Run Stage: 빌드된 JAR만 추출하여 실행 이미지를 만듭니다.
FROM eclipse-temurin:17

# 작업 디렉토리 설정
WORKDIR /app

# Build Stage에서 생성된 JAR 파일 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 컨테이너 메타데이터로 포트 정보 제공
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
