# 1단계: 빌드
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# 의존성 캐싱 (pom.xml이 바뀌지 않으면 이 레이어는 재사용)
COPY pom.xml .
RUN mvn dependency:go-offline -q

# 소스 복사 후 빌드
COPY src ./src
RUN mvn clean package -DskipTests -q

# 2단계: 실행 (JRE만 포함한 가벼운 이미지)
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/Quant-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
