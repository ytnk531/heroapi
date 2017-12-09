package models

import play.api.libs.json._

case class Hero(id: Int, name: String)

object Hero {
  implicit val personFormat = Json.format[Hero]
}
