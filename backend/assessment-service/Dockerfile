# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -B

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

RUN mkdir -p /app/uploads && chown appuser:appgroup /app/uploads

COPY --from=builder /app/target/assessment-service-0.0.1-SNAPSHOT.jar app.jar

USER appuser

EXPOSE 8087

ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-jar", "app.jar"]
