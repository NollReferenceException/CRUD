#FROM openjdk:18
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} CRUD-0.0.1-SNAPSHOT.jar
#CMD ["java", "-jar", "CRUD-0.0.1-SNAPSHOT.jar"]

FROM maven:3.8.7-openjdk-18-slim AS build
RUN mkdir -p /workspace
WORKDIR /workspace
COPY pom.xml /workspace
COPY src /workspace/src
RUN mvn -B -f pom.xml clean package -DskipTests

FROM openjdk:18-jdk-slim
COPY --from=build /workspace/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]