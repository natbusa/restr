import sbt._
import Keys._

object Dependencies {
  val akkaVersion       = "2.2.3"
  val sprayVersion      = "1.2.0"
  val sprayJsonVersion  = "1.2.5"
  val phantomVersion    = "1.2.2"

  val allDependencies = Seq(
    //spray
    "io.spray"           % "spray-can"       % sprayVersion,
    "io.spray"           % "spray-io"        % sprayVersion,
    "io.spray"           % "spray-httpx"     % sprayVersion,
    "io.spray"           % "spray-routing"   % sprayVersion,
    "io.spray"          %% "spray-json"      % sprayJsonVersion,
    
    //akka
    "com.typesafe.akka" %% "akka-actor"      % akkaVersion,
    "com.typesafe.akka" %% "akka-testkit"    % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j"      % akkaVersion,
    
    //phantom (cassandra)
    "com.websudos"      %% "phantom-dsl"         % phantomVersion,
    "com.websudos"      %% "phantom-testing"     % phantomVersion,
    "com.websudos"      %% "phantom-zookeeper"   % phantomVersion,

    //logging
    "ch.qos.logback"     % "logback-classic" % "1.0.13",    
    
    //testing
    "org.scalatest"      % "scalatest_2.10"  % "1.9.1" % "test",
    "junit"              % "junit"           % "4.11"  % "test"
  )
}

