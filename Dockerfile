FROM openjdk:8
ADD target/EVG-Driver-Server-0.0.1-SNAPSHOT.jar EVG-Driver-Server-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "EVG-Driver-Server-0.0.1-SNAPSHOT.jar"]
