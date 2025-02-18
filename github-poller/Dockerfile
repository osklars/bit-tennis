FROM sbtscala/scala-sbt:eclipse-temurin-17.0.13_11_1.10.7_3.6.3
COPY . /app
WORKDIR /app

# Add these lines to build before running
RUN sbt compile

CMD ["sbt", "run"]