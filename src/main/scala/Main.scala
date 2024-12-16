import scala. concurrent. duration. DurationInt
import cats.effect.{ExitCode, IO, IOApp, Ref}
import com.comcast.ip4s.*
import fs2.concurrent.Topic
import model.MatchState
import org.http4s.*
import org.http4s.ember.server.EmberServerBuilder

object Main extends IOApp:
  def server: IO[ExitCode] =
    for
      updates <- Topic[IO, List[StateHistory]]
      history <- Ref.of[IO, List[StateHistory]](List.empty)
      state <- Ref.of[IO, Option[MatchState]](None)
      _ <- EmberServerBuilder
        .default[IO]
        .withPort(port"8080")
        .withHost(host"0.0.0.0")
        .withHttpApp(Routes(StateManager(state, history, updates)).routes.orNotFound)
        .build
        .use(_ => IO.never)
        .handleErrorWith { error =>
          IO.println(s"Server crashed with error: ${error.getMessage}") *>
            IO.sleep(5.seconds) *>
            server  // Recursive call to restart
        }
    yield ExitCode.Success

  def run(args: List[String]): IO[ExitCode] = server