# FROM openjdk:17
# COPY target/guiFakturki-0.0.1-SNAPSHOT.jar app.jar
# ENTRYPOINT ["java","-jar","/app.jar"]

FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve
EXPOSE 8083
COPY src ./src
ENV API=api
CMD ["./mvnw", "spring-boot:run"]