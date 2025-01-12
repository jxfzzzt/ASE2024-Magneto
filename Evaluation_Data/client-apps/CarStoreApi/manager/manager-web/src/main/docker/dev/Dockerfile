#FROM openjdk:8-jdk-alpine
FROM java:latest

VOLUME /tmp

ADD manager.jar app.jar

ENTRYPOINT ["sh", "-c", "java -Xmx800m -Xms800m -Djava.security.egd=file:/dev/./urandom -Dfile.encoding=UTF8  -Duser.timezone=GMT+08 -jar /app.jar --spring.profiles.active=dev"]