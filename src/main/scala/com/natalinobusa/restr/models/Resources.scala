package com.natalinobusa.restr.models

import java.util.UUID

object Resources {
  case class User(id: Option[UUID], firstname: String, lastname: String, superpowers: List[String])
}

object Rest {
 // only when the representation differ from the resource

}

import spray.json.DefaultJsonProtocol
import com.natalinobusa.restr.utils.UuidMarshalling

object JsonConversions extends DefaultJsonProtocol with UuidMarshalling {

  import com.natalinobusa.restr.models.Resources._

  implicit val userFormat = jsonFormat4(User)
}
