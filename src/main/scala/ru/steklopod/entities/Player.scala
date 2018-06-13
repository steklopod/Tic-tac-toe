package ru.steklopod.entities

import javax.validation.constraints.Size
import scalikejdbc._

import scala.annotation.meta.field
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class Player(id: Option[Long],
                  @(Size@field)(min = 3, max = 20) username: String,
                  @(Size@field)(min = 8, max = 100) password: Option[String],
                  online: Boolean,
                  wins: Int,
                  losses: Int) {
  def this(username: String, password: String) {
    this(Option.empty[Long], username, Some(password), false, 0, 0)
  }
}

object Player extends SQLSyntaxSupport[Player] {

  override val tableName = "player"

  def apply(r: ResultName[Player])(rs: WrappedResultSet) =
    new Player(
      rs.longOpt(r.id),
      rs.string(r.username),
      rs.stringOpt(r.password),
      rs.boolean(r.online),
      rs.int(r.wins),
      rs.int(r.losses)
    )

  private val p = syntax

  def create(player: Player)(implicit session: DBSession = AutoSession): Future[Boolean] = {
    val sql = withSQL(insert.into(Player).namedValues(
      column.username -> player.username,
      column.password -> player.password.get,
      column.online -> false,
      column.wins -> 0,
      column.losses -> 0
    ))
    Future {
      sql.update().apply() == 1
    }
  }

  def findById(id: Long)(implicit s: DBSession = AutoSession): Future[Option[Player]] = Future {
    val sql = withSQL(
      select
        .from[Player](Player as p)
        .where.eq(p.id, id)
    )
    sql.map(Player(p.resultName)).headOption().apply()
  }

  def findByName(name: String)(implicit s: DBSession = AutoSession): Future[Option[Player]] = Future {
    val sql = withSQL(
      select
        .from[Player](Player as p)
        .where.eq(p.username, name)
    )
    sql.map(Player(p.resultName)).headOption().apply()
  }

  def findAll(max: Int, skip: Int): List[Player] = DB readOnly { implicit session =>
    val limitGames = withSQL {
      select.from(Player as p)
        .limit(max)
        .offset(skip)
    }
      .map(Player(p.resultName))
      .list
    limitGames.apply()
  }

}