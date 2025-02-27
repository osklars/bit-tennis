// src/main/scala/github/GithubClient.scala
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.*
import cats.effect.{IO, Resource}
import model.Content
import org.typelevel.ci.*
import upickle.default.*
import Codecs.upickleDecoder

class GithubClient(client: org.http4s.client.Client[IO]):
  def getTreeHash(repo: String, path: String, folder: String): IO[Option[String]] =
    val request = Request[IO](
      method = Method.GET,
      uri = Uri.unsafeFromString(s"https://api.github.com/repos/$repo/contents$path?ref=main"),
      headers = Headers(
        Header.Raw(ci"Accept", "application/vnd.github.v3+json")
      )
    )

    client.expect[List[Content]](request).map { response =>
      response.find(_.name == folder)
        .map(_.sha)
    }

object GithubClient:
  def make: Resource[IO, GithubClient] =
    EmberClientBuilder.default[IO].build.map(new GithubClient(_))