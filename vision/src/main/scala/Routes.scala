import cats.effect.IO
import model.api.out.Event
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.`Content-Type`
import org.http4s.server.middleware.{CORS, ErrorHandling}
import org.http4s.{EntityEncoder, Header, HttpRoutes, MediaType, Response}
import org.typelevel.ci.CIStringSyntax
import upickle.default.*

class Routes extends Http4sDsl[IO]:

  import Codecs.*

  private val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case req@POST -> Root / "video" =>
      req.body.chunks
        .evalTap(chunk => IO.println(s"Received chunk of size: ${chunk.size}"))
        .compile
        .drain
        .flatMap(_ => Ok("Stream completed"))
  }

  val corsRoutes: HttpRoutes[IO] = CORS.policy
    .withAllowOriginAll(ErrorHandling.httpRoutes(routes))