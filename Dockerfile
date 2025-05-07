# 1️⃣ Build Stage
FROM eclipse-temurin:17 AS builder

WORKDIR /app

COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle settings.gradle ./

RUN chmod +x ./gradlew
RUN ./gradlew dependencies --no-daemon

COPY . .

RUN ./gradlew build --no-daemon --parallel --build-cache -x test

# 2️⃣ Run Stage
FROM eclipse-temurin:17

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
