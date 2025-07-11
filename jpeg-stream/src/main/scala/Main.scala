import cats.effect.{ExitCode, IO, IOApp}
import com.comcast.ip4s.*
import org.http4s.*
import org.http4s.ember.server.EmberServerBuilder

object Main extends IOApp:

  def run(args: List[String]): IO[ExitCode] =
    for
      _ <- EmberServerBuilder
        .default[IO]
        .withPort(port"8081")
        .withHost(host"0.0.0.0")
        .withHttpApp(Routes.corsRoutes.orNotFound)
        .build
        .use(_ => IO.never)
    yield ExitCode.Success