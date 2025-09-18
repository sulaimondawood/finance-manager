FROM maven:3.9.11-eclipse-temurin-21-alpine as builder

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21

WORKDIR /app

COPY --from=builder /app/target/finance-manager-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9090

ENTRYPOINT [ "java","-jar", "app.jar" ]