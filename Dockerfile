#-------------------------------------------------------------
# Build

FROM gradle:6.3-jdk11 as builder

ENV GRADLE_HOME /usr/bin/gradle
ENV GRADLE_USER_HOME /cache

RUN apt update && apt install net-tools -y
RUN gradle --version && java -version

WORKDIR /app

# Only copy dependency-related files
COPY build.gradle gradle.properties settings.gradle /app/

# Only download dependencies
# Eat the expected build failure since no source code has been copied yet
RUN gradle clean build --no-daemon > /dev/null 2>&1 || true

# Copy all files
COPY ./ /app/

RUN gradle --no-daemon shadowJar

#----------------------------------------------------------------
# Run
FROM adoptopenjdk/openjdk11:alpine-jre

COPY --from=builder /app/build/libs/microchaos-1.0.0-all.jar ./microchaos.jar
CMD java -Xms64m -Xmx64m -jar microchaos.jar

EXPOSE 8080