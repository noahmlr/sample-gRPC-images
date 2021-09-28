FROM openjdk:17 AS GRADLE_BUILD

COPY ./ ./

RUN java --version
RUN chmod +x gradlew
RUN ./gradlew clean build

FROM openjdk:17

#COPY build/libs/jdk17-0.0.1-SNAPSHOT.jar /app.jar
COPY --from=GRADLE_BUILD build/libs/jdk17-client-0.0.1-SNAPSHOT.jar /app.jar

CMD ["java", "-jar", "/app.jar", "Noah", "host.docker.internal:50052"]