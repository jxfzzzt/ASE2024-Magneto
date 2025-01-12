FROM openjdk:8-jdk-alpine

VOLUME /tmp

ADD order.jar app.jar

#RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

ENTRYPOINT ["sh", "-c", "java -Xmx800m -Xms800m -Djava.security.egd=file:/dev/./urandom -Dfile.encoding=UTF8  -Duser.timezone=GMT+08 -jar /app.jar --spring.profiles.active=dev"]