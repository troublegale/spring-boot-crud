FROM openjdk:17-jdk-alpine
ARG JAR=build/libs/*.jar
COPY ${JAR} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]