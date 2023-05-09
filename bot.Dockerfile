FROM openjdk:17.0.1-jdk-slim

WORKDIR /app

COPY ./bot/target/bot-1.0-SNAPSHOT.jar bot.jar

ENTRYPOINT ["java","-jar","bot.jar"]