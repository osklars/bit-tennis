import cats.effect.Concurrent
import upickle.default.*

object Codecs:

  import org.http4s.*

  implicit def upickleEncoder[F[_], A: Writer]: EntityEncoder[F, A] =
    EntityEncoder.stringEncoder[F].contramap[A](write[A](_))

  implicit def upickleDecoder[F[_] : Concurrent, A: Reader]: EntityDecoder[F, A] =
    EntityDecoder.text[F].map(read[A](_))