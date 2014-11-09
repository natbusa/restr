package com.natalinobusa.restr.models

import java.util.UUID

import com.natalinobusa.restr.models.Resources.User
import spray.json.JsObject

object Messages {

  // generic messages for resources
  // nul- and idem- potent by id
  case class  Get(id:UUID)
  case class  Head(id:UUID)
  case class  Delete(id:UUID)

  // create with json
  case class  Create(item:User)

  // update with json
  case class  Update(id:UUID, item:JsObject)

  // list them all (todo: pagination)
  case object List

}
