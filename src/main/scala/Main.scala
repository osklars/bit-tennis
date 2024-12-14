import cats.effect.{ExitCode, IO, IOApp, Ref}
import com.comcast.ip4s.*
import fs2.concurrent.Topic
import org.http4s.*
import org.http4s.ember.server.EmberServerBuilder

object Main extends IOApp:
  def run(args: List[String]): IO[ExitCode] =
    for
      updates <- Topic[IO, List[GameState]]
      state <- Ref.of[IO, List[GameState]](List(GameState.initial(Player.A)))
      manager = StateManager(state, updates)
      routes = Routes[IO](manager).routes
      _ <- EmberServerBuilder
        .default[IO]
        .withPort(port"8080")
        .withHost(host"0.0.0.0")
        .withHttpApp(routes.orNotFound)
        .build
        .use(_ => IO.never)
    yield ExitCode.Success