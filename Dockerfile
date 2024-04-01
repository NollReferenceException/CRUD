FROM openjdk:18
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} CRUD-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar", "CRUD-0.0.1-SNAPSHOT.jar"]