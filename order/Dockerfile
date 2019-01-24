FROM openjdk:8-jdk-alpine
RUN apk add --update curl && rm -rf /var/cache/apk/*
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Dspring.profiles.active=docker","-Djava.security.egd=file:/dev/./urandom","-Xss256K","-Xms64M","-Xmx140M","-XX:MetaspaceSize=20M","-XX:MaxMetaspaceSize=60M","-jar","/app.jar"]
