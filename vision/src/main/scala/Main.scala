import cats.effect.{ExitCode, IO, IOApp, Ref}
import com.comcast.ip4s.*
import org.http4s.*
import org.http4s.ember.server.EmberServerBuilder

import scala.concurrent.duration.DurationInt

object Main extends IOApp:
  def server: IO[ExitCode] =
    for
      _ <- EmberServerBuilder
        .default[IO]
        .withPort(port"8082")
        .withHost(host"0.0.0.0")
        .withHttpApp(Routes().corsRoutes.orNotFound)
        .build
        .use(_ => IO.never)        
        .handleErrorWith { error =>
          IO.println(s"Server crashed with error: ${error.getMessage}") *>
            IO.sleep(5.seconds) *>
            server // Recursive call to restart
        }
    yield ExitCode.Success

  def run(args: List[String]): IO[ExitCode] = server