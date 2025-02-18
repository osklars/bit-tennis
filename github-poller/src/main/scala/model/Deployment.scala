package model

case class Deployment(
                       resourceId: String,
                       repo: String,
                       path: String,
                       lastHash: String
                     )

object models:
  given deploymentRW: upickle.default.ReadWriter[Deployment] = upickle.default.macroRW
