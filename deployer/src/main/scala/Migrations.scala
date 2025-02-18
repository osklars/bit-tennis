// src/main/scala/db/Migrations.scala
import org.flywaydb.core.Flyway
import cats.effect.{IO, Resource}

object Migrations:
  def run(url: String, user: String, password: String): IO[Unit] = IO {
    Flyway.configure()
      .dataSource(url, user, password)
      .load()
      .migrate()
  }.void