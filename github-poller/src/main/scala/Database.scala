// src/main/scala/db/Database.scala
import cats.effect.{IO, Resource}
import model.Deployment
import skunk.*
import skunk.implicits.*
import skunk.codec.all.*
import natchez.Trace.Implicits.noop

trait Database:
  def getDeployments: IO[List[Deployment]]
  def updateHash(resourceId: String, newHash: String): IO[Unit]

class PostgresDatabase(session: Session[IO]) extends Database:
  private val getAllQuery =
    sql"SELECT resource_id, repo, path, last_hash FROM deployments"
      .query(varchar ~ varchar ~ varchar ~ varchar)
      .map { case rid ~ repo ~ path ~ hash =>
        Deployment(rid, repo, path, hash)
      }

  private val updateHashCommand =
    sql"""
      UPDATE deployments 
      SET last_hash = $varchar, last_check = NOW() 
      WHERE resource_id = $varchar
    """.command

  def getDeployments: IO[List[Deployment]] =
    session.execute(getAllQuery)

  def updateHash(resourceId: String, newHash: String): IO[Unit] =
    session.prepare(updateHashCommand).flatMap { cmd =>
      cmd.execute(newHash ~ resourceId).void
    }

object Database:
  def make(config: String): Resource[IO, Database] =
    Session.single[IO](
      host = "localhost",
      port = 5432,
      user = "postgres",
      database = "deployments",
      password = Some("password")
    ).map(PostgresDatabase(_))