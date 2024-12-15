import cats.effect.{Concurrent, IO}
import cats.syntax.all.*
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.`Content-Type`
import org.http4s.server.middleware.CORS
import org.http4s.{EntityEncoder, Header, HttpRoutes, MediaType}
import org.typelevel.ci.CIStringSyntax
import upickle.default.*

class Routes(manager: StateManager[IO]) extends Http4sDsl[IO]:
  import Codecs.*

  val routes: HttpRoutes[IO] = CORS.policy
    .withAllowOriginAll(HttpRoutes.of[IO] {
      case req@POST -> Root / "event" =>
        for
          _ <- IO.println(req.headers.headers.map(h => s"Header: ${h.name} = ${h.value}"))
          event <- req.as[GameEvent]
          _ <- IO.println(s"Incoming event: $event")
          state <- manager.process(event)
          _ <- IO.println(s"New State: $state")
          resp <- Ok(state)
        yield resp

      case GET -> Root / "state" =>
        val stream = manager.subscribe
          .map(state => write(state))
          .through(fs2.text.utf8.encode)

        Ok(stream).map(_.withHeaders(
          `Content-Type`(MediaType.`text/event-stream`),
          Header.Raw(ci"Cache-Control", "no-cache"),
          Header.Raw(ci"Connection", "keep-alive")
        ))
    })