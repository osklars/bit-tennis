import cats.effect.{IO, IOApp}

import scala.concurrent.duration.DurationInt

object Main extends IOApp.Simple:
  def run: IO[Unit] =
    val dbConfig = DbConfig(
      host = sys.env("DB_HOST"),
      port = sys.env("DB_PORT").toInt,
      user = sys.env("DB_USER"),
      password = sys.env("DB_PASSWORD"),
      database = sys.env("DB_NAME")
    )

    val resources = for
      db <- Database.make(dbConfig)
      github <- GithubClient.make
      coolify <- CoolifyClient.make(
        sys.env("COOLIFY_URL"),
        sys.env("COOLIFY_TOKEN")
      )
    yield new PollerService(db, github, coolify)

    for {
//      _ <- Migrations.run(sys.env("DB_URL"), sys.env("DB_USER"), sys.env("DB_PASSWORD"))
      _ <- resources.use { poller =>
        poller.pollStream(30.seconds).compile.drain
      }
    } yield ()