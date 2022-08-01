#FROM adoptopenjdk:17-jre-hotspot
FROM openjdk:19-jdk
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]