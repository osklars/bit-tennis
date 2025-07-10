import cats.effect.IO
import fs2.io.file.Files
import fs2.io.file.Path
import fs2.{Chunk, Pipe}

object ImageConverter {
  def chunkJpegFrames: Pipe[IO, Byte, Chunk[Byte]] =
    _.chunks
      .scan((Chunk.empty[Byte], Option.empty[Chunk[Byte]])) { case ((buffer, _), chunk) =>
        val combined = buffer ++ chunk
        val jpegStart = combined.indexWhere(_ == 0xFF.toByte)
        val jpegEnd = combined.toArray.sliding(2).indexWhere(_.sameElements(Array(0xFF.toByte, 0xD9.toByte)))

        if (jpegStart.nonEmpty && jpegEnd >= 0) {
          val frame = combined.take(jpegEnd + 2)
          val remaining = combined.drop(jpegEnd + 2)
          (remaining, Some(frame))
        } else {
          (combined, None)
        }
      }
      .collect { case (_, Some(frame)) => frame }

  def processFrame(frame: Chunk[Byte]): IO[Unit] =
    for
      _ <- IO.println(s"Processing frame: ${frame.size} bytes")
      _ <- Files[IO].writeAll(Path("output.jpeg"))(fs2.Stream.chunk(frame)).compile.drain
    yield ()
}
