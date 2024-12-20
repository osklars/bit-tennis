import cats.effect.{ExitCode, IO, IOApp, Ref}
import com.comcast.ip4s.*
import fs2.concurrent.Topic
import model.api.out.StateSummary
import model.pingis.MatchState
import org.http4s.*
import org.http4s.ember.server.EmberServerBuilder

import scala.concurrent.duration.DurationInt

object Main extends IOApp:
  def server: IO[ExitCode] =
    for
      state <- Ref.of[IO, MatchState](MatchState())
      updates <- Topic[IO, StateSummary]
      _ <- EmberServerBuilder
        .default[IO]
        .withPort(port"8080")
        .withHost(host"0.0.0.0")
        .withHttpApp(Routes(StateService(state, updates)).corsRoutes.orNotFound)
        .build
        .use(_ => IO.never)
        .handleErrorWith { error =>
          IO.println(s"Server crashed with error: ${error.getMessage}") *>
            IO.sleep(5.seconds) *>
            server // Recursive call to restart
        }
    yield ExitCode.Success

  def run(args: List[String]): IO[ExitCode] = server