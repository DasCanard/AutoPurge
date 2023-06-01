FROM gradle:7.4.2-jdk17 AS build

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM amazoncorretto:17

ENV BOT_TOKEN=""
ENV PURGE_CHANNELS=""
#Optional
#ENV INTERVAL_MINUTES=60

RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/ /app/

ENTRYPOINT ["java", "-Denv=prod", "-jar", "/app/AutoPurge.jar"]