FROM gradle:7.6.0-jdk17-alpine AS builder

ARG FLYWAY_URL
ARG FLYWAY_USER
ARG FLYWAY_PASSWORD

ADD . /usr/local/opt/app
WORKDIR /usr/local/opt/app

RUN gradle build && \
    cd build/libs && \
    java -Djarmode=layertools -jar seat-mowers.jar extract

FROM eclipse-temurin:17.0.5_8-jre-alpine

WORKDIR /usr/local/opt/app

COPY --from=builder /usr/local/opt/app/build/libs/dependencies/ ./
COPY --from=builder /usr/local/opt/app/build/libs/spring-boot-loader/ ./
COPY --from=builder /usr/local/opt/app/build/libs/snapshot-dependencies/ ./
COPY --from=builder /usr/local/opt/app/build/libs/application/ ./

EXPOSE 8080

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]