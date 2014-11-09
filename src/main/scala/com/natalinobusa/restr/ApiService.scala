package com.natalinobusa.restr

import scala.concurrent.duration._
import scala.util.{Failure, Success}

// akka
import akka.actor.{ActorLogging, ActorSelection, Actor}
import akka.pattern.ask
import akka.util.Timeout

// spray
import spray.routing.HttpService
import spray.http.StatusCodes

// marshalling/unmarshalling from http body (wire) to json and back
import spray.httpx.SprayJsonSupport.sprayJsonUnmarshaller

// Resources
import models.Resources._

// Resources to json and back
import models.JsonConversions._

// Actor Messages
import com.natalinobusa.restr.models.Messages._

class ApiServiceActor() extends Actor with ApiService with ActorLogging {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing,
  // timeout handling or alternative handler registration
  def receive = runRoute(serviceRoute)

}

// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
trait ApiService extends HttpService {

  implicit val ec = actorRefFactory.dispatcher
  implicit val timeout = Timeout(1.seconds)

  def userActor = actorRefFactory.actorSelection("/user/users")

  // just a few handy shortcut
  //def Ask(a: ActorPath, msg:Any) =  actorRefFactory.actorSelection(a).ask(msg)
  //def Ask(a: String, msg:Any)    =  actorRefFactory.actorSelection(a).ask(msg)
  def Ask(a: ActorSelection, msg: Any) = a.ask(msg)

  val serviceRoute = {
    pathPrefix("api" / "users") {
      // curl -H "Content-Type: application/json" -d '{"id":"5ba50eeb-69e3-462f-8e4f-ad680dcb3757", "firstname":"Natalino", "lastname":"Busa", "superpowers":["data science", "parallel computing", "spaghetti with sepias"]}' 127.0.0.1:8080/api/users
      post {
        entity(as[User]) { req =>
          onComplete(userActor.ask(Create(req)).mapTo[Option[User]]) {
            case Success(Some(res)) => complete(StatusCodes.Created, res)
            case Failure(ex) => complete(StatusCodes.Conflict)
            case _ => complete(StatusCodes.InternalServerError)
          }
        }
      } ~
        pathPrefix(JavaUUID ) { id =>
          get {
              onComplete(userActor.ask(Get(id)).mapTo[Option[User]]) {
                case Success(Some(res)) => complete(StatusCodes.OK, res)
                case Success(None)      => complete(StatusCodes.NotFound)
              }
          } ~
          delete {
            onComplete(userActor.ask(Delete(id)).mapTo[Boolean]) {
              case Success(_) => complete(StatusCodes.NoContent)
            }
          }
        }
    }
  }

}

