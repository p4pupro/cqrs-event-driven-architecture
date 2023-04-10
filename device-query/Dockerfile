FROM maven:3.8.1-openjdk-17-slim AS build
COPY pom.xml .
COPY device-query/pom.xml ./device-query/
RUN mvn -f device-query/pom.xml dependency:go-offline
COPY device-query/src ./device-query/src
WORKDIR device-query/
RUN mvn clean package -DskipTests

FROM amazoncorretto:17-al2023-jdk
COPY --from=build device-query/target/device-query-0.0.1-SNAPSHOT.jar device-query.jar
ENTRYPOINT ["java", "-jar", "device-query.jar"]