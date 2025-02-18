import cats.effect.{ExitCode, IO, IOApp, Ref}
import com.comcast.ip4s.*
import fs2.concurrent.Topic
import org.http4s.*
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder

import scala.concurrent.duration.DurationInt

object Main extends IOApp.Simple:
  def run: IO[Unit] =
    val resources = for
      db <- Database.make("jdbc:postgresql://localhost:5432/deployments")
      github <- GithubClient.make(sys.env("GITHUB_TOKEN"))
      coolify <- CoolifyClient.make(
        sys.env("COOLIFY_URL"),
        sys.env("COOLIFY_TOKEN")
      )
    yield new PollerService(db, github, coolify)

    resources.use { poller =>
      poller.pollStream(30.seconds).compile.drain
    }