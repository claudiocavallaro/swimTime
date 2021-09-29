FROM openjdk:oraclelinux8
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-DdbEnv=docker","-jar","/app.jar"]