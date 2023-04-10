FROM maven:3.8.1-openjdk-17-slim AS build
COPY pom.xml .
COPY device-command/pom.xml ./device-command/
RUN mvn -f device-command/pom.xml dependency:go-offline
COPY device-command/src ./device-command/src
WORKDIR device-command/
RUN mvn clean package -DskipTests

FROM amazoncorretto:17-al2023-jdk
COPY --from=build device-command/target/device-command-0.0.1-SNAPSHOT.jar device-command.jar
ENTRYPOINT ["java", "-jar", "device-command.jar"]