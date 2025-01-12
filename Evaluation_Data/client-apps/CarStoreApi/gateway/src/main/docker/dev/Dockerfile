FROM openjdk:8-jdk-alpine

VOLUME /tmp

ADD gateway.jar app.jar

ENTRYPOINT ["sh", "-c", "java -Xmx400m -Xms400m -Djava.security.egd=file:/dev/./urandom -Dfile.encoding=UTF8  -Duser.timezone=GMT+08 -jar /app.jar --spring.profiles.active=dev"]