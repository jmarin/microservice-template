import sbt._
import sbt.Keys._
import com.typesafe.sbt.SbtScalariform._
import spray.revolver.RevolverPlugin._

object BuildSettings {
  val buildOrganization = "cfpb"
  val buildVersion      = "0.0.1"
  val buildScalaVersion = "2.11.6"

  val buildSettings = Defaults.coreDefaultSettings ++
    scalariformSettings ++
    Seq(
      organization  := buildOrganization,
      version       := buildVersion,
      scalaVersion  := buildScalaVersion,
      scalacOptions := Seq("-deprecation", "-unchecked", "-feature")
    )
}

object TemplateBuild extends Build {
  import Dependencies._
  import BuildSettings._

  val testDeps = Seq(scalaTest, scalaCheck)

  val akkaDeps = testDeps ++ Seq(akkaActor, akkaStreams)

  val akkaHttpDeps = akkaDeps ++ Seq(akkaHttp, akkaHttpCore, akkaHttpTestkit, akkaHttpJson, logback, scalaLogging, logstashLogback)

  val kamon = Seq(kamonCore, kamonStatsd)

  val deps = akkaHttpDeps ++ kamon

  lazy val microservice = Project(
    "microservice-template",
    file("."),
    settings = buildSettings ++ Revolver.settings ++ Seq(libraryDependencies ++= deps, resolvers ++= repos)
  )

}
