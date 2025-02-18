// src/main/scala/DeploymentPoller.scala
import cats.effect.IO
import cats.instances.list.*
import cats.syntax.traverse.*
import fs2.Stream
import model.Deployment

import scala.concurrent.duration.*

class PollerService(
                     db: Database,
                     github: GithubClient,
                     coolify: CoolifyClient
                   ):
  def poll: IO[Unit] =
    for
      deployments <- db.getDeployments
      _ <- deployments.traverse(d => run(d).attempt.recoverWith(e => IO.println(s"Failed item $d with ${e.getMessage}")))
    yield ()

  def run(deployment: Deployment): IO[Unit] =
    for
      currentHash <- github.getTreeHash(deployment.repo, deployment.path)
      _ <- if currentHash != deployment.lastHash then
        db.updateHash(deployment.resourceId, currentHash) *>
          coolify.triggerDeploy(deployment.resourceId)
      else IO.unit
    yield ()


  def pollStream(interval: FiniteDuration): Stream[IO, Unit] =
    Stream
      .repeatEval(poll.attempt.flatMap {
        case Left(e) => IO.println(s"Failed poll with ${e.getMessage}")
        case _ => IO.unit
      })
      .metered(interval)
