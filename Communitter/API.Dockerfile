FROM openjdk:17.0.2-slim-bullseye
WORKDIR /app
COPY . .
RUN ./mvnw clean install -DskipTests
CMD java -jar ./target/Communitter-0.0.1-SNAPSHOT.jar