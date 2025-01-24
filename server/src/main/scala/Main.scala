import cats.effect.{ExitCode, IO, IOApp, Ref}
import com.comcast.ip4s.*
import fs2.concurrent.Topic
import model.api.out.StateSummary
import model.pingis.MatchState
import org.http4s.*
import org.http4s.ember.server.EmberServerBuilder

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
    yield ExitCode.Success

  def run(args: List[String]): IO[ExitCode] = server