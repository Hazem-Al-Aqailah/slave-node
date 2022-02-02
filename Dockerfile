FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/node-0.0.1-SNAPSHOT.jar node-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","node-0.0.1-SNAPSHOT.jar"]
