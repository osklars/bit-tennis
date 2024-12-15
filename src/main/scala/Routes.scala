import cats.effect.IO
import cats.implicits.catsSyntaxApplyOps
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.`Content-Type`
import org.http4s.server.middleware.CORS
import org.http4s.{EntityEncoder, Header, HttpRoutes, MediaType, Response}
import org.typelevel.ci.CIStringSyntax
import upickle.default.*

class Routes(manager: StateManager[IO]) extends Http4sDsl[IO]:

  import Codecs.*

  private def handleError[A](io: IO[Response[IO]]): IO[Response[IO]] =
    io.handleErrorWith { error =>
      val errorMessage = s"An error occurred: ${error.getMessage}"
      IO.println(errorMessage) *>
        InternalServerError(write(ErrorResponse(errorMessage)))
    }

  // Custom error response class
  case class ErrorResponse(error: String)derives ReadWriter

  val routes: HttpRoutes[IO] = CORS.policy
    .withAllowOriginAll(HttpRoutes.of[IO] {
      case req@POST -> Root / "event" =>
        handleError(
          for
            event <- req.as[GameEvent]
            _ <- IO.println(s"Incoming event: $event")
            state <- manager.process(event)
            _ <- IO.println(s"Returning new State: ${write(state)}")
            resp <- Ok(state)
          yield resp
        )

      case GET -> Root / "state" =>
        val stream =
          (fs2.Stream.eval(manager.getHistory.map(_.take(5))) ++ manager.subscribe)
            .evalMap(history => IO.println(s"streaming: $history \n${write(history)}").map(_ => history))
            .map(history => s"data: ${write(history)}\n\n")
            .through(fs2.text.utf8.encode)
            .handleErrorWith { error =>
              fs2.Stream.eval(
                IO.println(s"Stream error: ${error.getMessage}")
              ) *>
                fs2.Stream.empty
            }

        Ok(stream).map(_.withHeaders(
          `Content-Type`(MediaType.`text/event-stream`),
          Header.Raw(ci"Cache-Control", "no-cache"),
          Header.Raw(ci"Connection", "keep-alive")
        ))
    })