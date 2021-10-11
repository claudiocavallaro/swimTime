FROM openjdk:11.0.1
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-DdbEnv=docker","-jar","/app.jar"]
