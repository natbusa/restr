package com.natalinobusa.restr

import java.util.UUID
import scala.concurrent.Future
import scala.concurrent.duration._

import akka.actor.{ActorLogging, Actor}
import com.natalinobusa.restr.models.Messages.{Delete, Get, Create}
import models.Resources.User

import models.Records.UserRecord

import akka.pattern.pipe
import scala.util.{Failure, Success}

class UserActor extends Actor with ActorLogging {

  implicit def ec = context.dispatcher

  def receive = {

    case Create(item:User) =>
      // validate the fields
      // generate the id
      // persist it via the dao

      // if id not provided, generate a new one
      val id  = item.id.getOrElse(UUID.randomUUID())
      val resource = item.copy(id=Some(id))

      log.debug("creating my user {}", resource.toString)

      val f = UserRecord.create(resource)

      log.debug("my future {}", f.toString)

      //      val f  = Future { Some(resource)}
      f pipeTo sender

    case Get(id) =>
      // validate the fields
      // generate the id
      // persist it via the dao
      //val resource = item.copy(id=Some(UUID.randomUUID()))

      val f = UserRecord.get(id)

      log.debug("my get future {}", f.toString)

      //      val f  = Future { Some(resource)}
      f pipeTo sender

    case Delete(id) =>
      // validate the fields
      // generate the id
      // persist it via the dao
      //val resource = item.copy(id=Some(UUID.randomUUID()))

      val f = UserRecord.remove(id)

      log.debug("my future {}", f.toString)

      //      val f  = Future { Some(resource)}
      f pipeTo sender

  }

}
