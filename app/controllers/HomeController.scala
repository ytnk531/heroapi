package controllers

import javax.inject._
import play.api._
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
import play.api.db._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json._
import play.api.libs.functional.syntax._

import models._

@Singleton
class HomeController @Inject()(
  repo: HeroRepository,
  cc: ControllerComponents
)(implicit ec: ExecutionContext) extends AbstractController(cc) {
  implicit val heroWrites: Writes[Hero] = (
    (JsPath \ "id").write[Int] and
    (JsPath \ "name").write[String]
  )(unlift(Hero.unapply))

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def showHero(id: Int) = Action.async { 
    implicit req =>
    {
      repo.getById(id).map { hero =>
        Ok(Json.toJson(hero))
      }
    }
  }

  def showHeroes = Action.async {
    implicit req =>
        repo.list.map { heroes => Ok(Json.toJson(heroes)) }
  }

  def searchHeroes(name: String) = Action.async {
    implicit req =>
        repo.search(name).map { heroes => Ok(Json.toJson(heroes)) }
  }

  def createHero() = Action.async(parse.json) { 
    implicit request => {
      repo.insert(
        Hero(1, (request.body \ "name").as[String])
      ).flatMap { i =>
        repo.getById(i).map {hero=>
          Ok(Json.toJson(hero))
        }
      }
    }
  }

  def deleteHero(id: Int) = Action.async {
    implicit request => {
      repo.delete(id)
        .map { ids => Ok(Json.toJson(ids)) }
      //req.body.validate[Hero].map {
      //case (id, name) => repo.insert(Hero(id, name)).map{hero => Ok(Json.toJson(hero))}
      }
    }

  def updateHero = Action.async(parse.json) {
    implicit request => {
      repo.update(Hero((request.body \ "id").as[Int], (request.body \ "name").as[String]))
        .map { ids => Ok(Json.toJson(ids)) }
    }
  }
}
