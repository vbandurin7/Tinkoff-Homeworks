FROM openjdk:17.0.1-jdk-slim

WORKDIR /app

COPY ./scrapper/target/scrapper-1.0-SNAPSHOT.jar scrapper.jar

ENTRYPOINT ["java","-jar","scrapper.jar"]