#FROM openjdk:8-jdk-alpine
FROM java:latest

VOLUME /tmp

ADD manager.jar app.jar

ENTRYPOINT ["sh", "-c", "java -Xmx2048m -Xms2048m -Djava.security.egd=file:/dev/./urandom -Dfile.encoding=UTF8  -Duser.timezone=GMT+08 -jar /app.jar --spring.profiles.active=pro"]