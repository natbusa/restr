import sbt._
import Keys._

object Resolvers {
  val allResolvers = Seq (
    "Spray Repository"        at "http://repo.spray.io",
    "Typesafe Repository"     at "http://repo.typesafe.com/typesafe/releases/",
    "Websudos releases"       at "http://maven.websudos.co.uk/ext-release-local"
  )
}

