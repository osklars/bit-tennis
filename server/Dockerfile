FROM sbtscala/scala-sbt:eclipse-temurin-17.0.13_11_1.10.7_3.6.3 AS builder
COPY . /build
WORKDIR /build
RUN sbt compile package

FROM eclipse-temurin:17-jre-jammy
COPY --from=builder /build/target/scala-*/pingu_*.jar /app/app.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/app.jar"]