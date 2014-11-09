package com.natalinobusa.restr.models

import java.util.UUID
import scala.concurrent.Future

import com.datastax.driver.core.{ResultSet, Row}

import com.websudos.phantom.Implicits._
import com.websudos.phantom.zookeeper.SimpleCassandraConnector


import Resources._

import scala.util.{Failure, Success}

object Records {

  trait RestrCassandraConnector extends SimpleCassandraConnector {
    val keySpace = "restr"
  }

  sealed class UserRecord extends CassandraTable[UserRecord, User]  {

    object id           extends UUIDColumn(this) with PartitionKey[UUID]
    object firstname    extends StringColumn(this)
    object lastname     extends StringColumn(this)
    object superpowers  extends ListColumn[UserRecord, User, String](this)

    override def fromRow(row: Row): User = {
      User(Some(id(row)), firstname(row), lastname(row), superpowers(row));
    }
  }


  object UserRecord extends UserRecord with RestrCassandraConnector {
    override val tableName = "users"

    def get(id: UUID): Future[Option[User]] = {
      select.where(_.id eqs id).one()
    }

    def remove(id: UUID) = {
      delete.where(_.id eqs id).future()
    }

    def create(v: User) = {
      insert
        .value(_.id, v.id.getOrElse(null))
        .value(_.firstname, v.firstname)
        .value(_.lastname, v.lastname)
        .value(_.superpowers, v.superpowers)
        //.ifNotExists()
        .future()
        // map map the future of Future[ResultRows] to a Future[Option[User]]
        .flatMap {
          rows => {
            for {
              one <- UserRecord.select.where(_.id eqs v.id.getOrElse(null)).one
            } yield one
          }
        }
    }

  }
}
