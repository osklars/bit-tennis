import cats.effect.*
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.server.middleware.{CORS, ErrorHandling}
import fs2.*
import org.http4s.dsl.Http4sDsl

import java.nio.file.Paths

object Routes extends Http4sDsl[IO]:

  private val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case req@POST -> Root / "stream" =>
      for
        _ <- IO.println("Starting video stream processing...")
        path <- IO.pure(Paths.get("output.avi"))
        _ <- req.body
          .through(ImageConverter.chunkJpegFrames)
          .evalMap(ImageConverter.processFrame) // Your CV analysis here
          .compile
          .drain
        _ <- IO.println("Finished processing video stream")
        resp <- Ok("Stream processed successfully")
      yield resp
  }

  val corsRoutes: HttpRoutes[IO] = CORS.policy
    .withAllowOriginAll(ErrorHandling.httpRoutes(routes))