#-------------------------------------------------------------
# Build

FROM gradle:6.3-jdk11 as builder

RUN apt update && apt install net-tools -y

ENV GRADLE_HOME /usr/bin/gradle
ENV GRADLE_USER_HOME /cache

COPY build.gradle gradle.properties settings.gradle ./
# Trick to force Gradle to download dependencies and cache it
RUN mkdir -p src/main/java
RUN sh -c "echo 'public class Dummy {}' > src/main/java/Dummy.java && gradle -i compileJava"

COPY src/  ./src
RUN gradle -i shadowJar

# expose for testcontainers
VOLUME /var/run/docker.sock

#----------------------------------------------------------------
# Run

FROM adoptopenjdk/openjdk11:alpine-jre

COPY --from=builder /home/gradle/build/libs/microchaos-1.0.0-all.jar ./microchaos.jar
CMD java -jar microchaos.jar