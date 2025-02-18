// src/main/scala/db/Database.scala

import cats.effect.{IO, Resource}
import model.{DbConfig, Deployment}
import natchez.Trace.Implicits.noop
import skunk.*
import skunk.codec.all.*
import skunk.implicits.*

class Database(session: Session[IO]):
  private val getAllQuery =
    sql"SELECT resource_id, repo, path, last_hash FROM deployments"
      .query(text ~ text ~ text ~ text)
      .map { case rid ~ repo ~ path ~ hash =>
        Deployment(rid, repo, path, hash)
      }

  private val updateLastCheckCommand =
    sql"""
      UPDATE deployments
      SET last_check = NOW()
      WHERE resource_id = $varchar
    """.command

  private val updateHashCommand =
    sql"""
      UPDATE deployments
      SET last_hash = $varchar, last_deployment = NOW()
      WHERE resource_id = $varchar
    """.command

  def getDeployments: IO[List[Deployment]] =
    session.execute(getAllQuery)

  def updateLastCheck(resourceId: String): IO[Unit] =
    session.prepare(updateLastCheckCommand).flatMap(_.execute(resourceId).void)

  def updateHash(resourceId: String, newHash: String): IO[Unit] =
    session.prepare(updateHashCommand).flatMap(_.execute(newHash ~ resourceId).void)

object Database:
  def make(config: DbConfig): Resource[IO, Database] =
    Session.single[IO](
      host = config.host,
      port = config.port,
      user = config.user,
      database = config.database,
      password = Some(config.password)
    ).map(Database(_))