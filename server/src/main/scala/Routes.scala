import cats.effect.IO
import model.InvalidEvent
import model.api.in.{Event, NewMatch}
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.`Content-Type`
import org.http4s.server.middleware.{CORS, ErrorHandling}
import org.http4s.{EntityEncoder, Header, HttpRoutes, MediaType, Response}
import org.typelevel.ci.CIStringSyntax
import upickle.default.*

class Routes(service: StateService) extends Http4sDsl[IO]:

  import Codecs.*

  private val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case req@POST -> Root / "new" =>
      for
        input <- req.as[NewMatch]
        _ <- IO.println(s"Starting new match: $input")
        state <- service.newMatch(input)
        resp <- Ok(state)
      yield resp

    case req@POST -> Root / "event" =>
      for
        event <- req.as[Event]
        result <- service.process(event)
          .map(Right.apply)
          .recover {
            case i: InvalidEvent => Left(i)
          }
        resp <- result match
          case Left(value) => NotAcceptable(value)
          case Right(value) => Ok(value)
      yield resp

    case GET -> Root / "state" =>
      val stream =
        (fs2.Stream.eval(service.getState) ++ service.subscribe)
          .evalMap(history => IO.println(s"streaming: $history \n${write(history)}").map(_ => history))
          .map(history => s"data: ${write(history)}\n\n")
          .through(fs2.text.utf8.encode)

      Ok(stream).map(_.withHeaders(
        `Content-Type`(MediaType.`text/event-stream`),
        Header.Raw(ci"Cache-Control", "no-cache"),
        Header.Raw(ci"Connection", "keep-alive")
      ))
  }

  val corsRoutes: HttpRoutes[IO] = CORS.policy
    .withAllowOriginAll(ErrorHandling.httpRoutes(routes))