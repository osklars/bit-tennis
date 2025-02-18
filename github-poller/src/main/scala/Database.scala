// src/main/scala/db/Database.scala
import cats.effect.{IO, Resource}
import model.Deployment
import skunk.*
import skunk.implicits.*
import skunk.codec.all.*
import natchez.Trace.Implicits.noop

class Database(session: Session[IO]):
  private val getAllQuery =
    sql"SELECT resource_id, repo, path, last_hash FROM deployments"
      .query(text ~ text ~ text ~ text)
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


case class DbConfig(
                     host: String,
                     port: Int,
                     user: String,
                     password: String,
                     database: String
                   )

object Database:
  def make(config: DbConfig): Resource[IO, Database] =
    Session.single[IO](
      host = config.host,
      port = config.port,
      user = config.user,
      database = config.database,
      password = Some(config.password)
    ).map(Database(_))