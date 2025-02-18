// src/main/scala/DeploymentPoller.scala

import cats.effect.IO
import cats.instances.list.*
import cats.syntax.option.*
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
      _ <- IO.println("Running poll")
      deployments <- db.getDeployments
      _ <- deployments.traverse(d => run(d).recoverWith(e => IO.println(s"Failed $d with ${e.getMessage}")))
    yield ()

  def run(deployment: Deployment): IO[Unit] =
    for
      _ <- IO.println(s"Running $deployment")
      (parentPath, folderName) = deployment.splitLastSegment
      currentHashOpt <- github.getTreeHash(deployment.repo, parentPath, folderName)
      currentHash <- currentHashOpt
        .liftTo[IO](new Exception(s"Could not find hash for $deployment"))
      _ <- db.updateLastCheck(deployment.resourceId)
      _ <- if currentHash != deployment.lastHash then
        coolify.triggerDeploy(deployment.resourceId) *>
          db.updateHash(deployment.resourceId, currentHash) *>
          IO.println(s"triggered $deployment")
      else IO.unit
    yield ()

  def pollStream(interval: FiniteDuration): Stream[IO, Unit] =
    Stream
      .repeatEval(poll.recoverWith(e => IO.println(s"Failed poll with ${e.getMessage}")))
      .metered(interval)
