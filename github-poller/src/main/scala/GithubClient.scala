// src/main/scala/github/GithubClient.scala
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.*
import org.http4s.headers.*
import cats.effect.{IO, Resource}
import org.typelevel.ci.*

class GithubClient(token: String, client: org.http4s.client.Client[IO]):
  def getTreeHash(repo: String, path: String): IO[String] =
    val request = Request[IO](
      method = Method.GET,
      uri = Uri.unsafeFromString(s"https://api.github.com/repos/$repo/contents/$path"),
      headers = Headers(
        Authorization(Credentials.Token(AuthScheme.Bearer, token)),
        Header.Raw(ci"Accept", "application/vnd.github.v3+json")
      )
    )

    client.expect[String](request).map { response =>
      // Parse response with upickle and extract sha
      ujson.read(response).obj("sha").str
    }

object GithubClient:
  def make(token: String): Resource[IO, GithubClient] =
    EmberClientBuilder.default[IO].build.map(new GithubClient(token, _))