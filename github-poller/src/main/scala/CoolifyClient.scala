import cats.effect.{IO, Resource}
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.headers.Authorization
import org.http4s.{AuthScheme, Credentials, Headers, Method, Request, Uri}

// src/main/scala/coolify/CoolifyClient.scala
class CoolifyClient(baseUrl: String, token: String, client: org.http4s.client.Client[IO]):
  def triggerDeploy(resourceId: String): IO[Unit] =
    val request = Request[IO](
      method = Method.POST,
      uri = Uri.unsafeFromString("$baseUrl/api/v1/resources/$resourceId/deploy"),
      headers = Headers(Authorization(Credentials.Token(AuthScheme.Bearer, token)))
    )
    client.expect[String](request).void

object CoolifyClient:
  def make(baseUrl: String, token: String): Resource[IO, CoolifyClient] =
    EmberClientBuilder.default[IO].build.map(new CoolifyClient(baseUrl, token, _))