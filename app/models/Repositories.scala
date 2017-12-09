package models

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import models._

import scala.concurrent.{ Future, ExecutionContext }

@Singleton
class HeroRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._


  private class HeroesTable(tag: Tag) extends Table[Hero](tag, "Hero") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    def * = (id, name) <> ((Hero.apply _).tupled, Hero.unapply)
  }

  private val heroes = TableQuery[HeroesTable]

  def list(): Future[Seq[Hero]] = db.run {
    heroes.result
  }

  def getById(id: Int): Future[Option[Hero]] = db.run {
    heroes.filter(_.id === id).result.headOption
  }

  def insert(hero: Hero): Future[Int] = db.run {
    //heroes.map(c => (c.name)) += (hero.name)
    (heroes returning heroes.map(_.id)) += hero
  }

  def delete(id: Int): Future[Int] = db.run {
    heroes.filter(_.id === id).delete
  }

  def update(hero: Hero): Future[Int] = db.run {
    heroes.filter(_.id === hero.id).update(hero)
  }

  def search(name: String): Future[Seq[Hero]] = db.run {
    heroes.filter(_.name.toLowerCase like ("%" + name + "%").toLowerCase).result
  }
}
