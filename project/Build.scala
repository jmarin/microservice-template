import sbt._
import sbt.Keys._
import com.typesafe.sbt.SbtScalariform._
import spray.revolver.RevolverPlugin._
import sbtassembly.AssemblyPlugin.autoImport._

object BuildSettings {
  val buildOrganization = "cfpb"
  val buildVersion      = "0.0.1"
  val buildScalaVersion = "2.11.8"

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

  val akkaHttpDeps = Seq(akkaHttp, akkaHttpTestkit, akkaHttpJson, logback, scalaLogging)

  val deps = akkaHttpDeps ++ testDeps

  lazy val microservice = Project(
    "microservice-template",
    file("."),
    settings =
      buildSettings ++
        Revolver.settings ++
        Seq(
          assemblyJarName in assembly := {s"${name.value}.jar"},
          libraryDependencies ++= deps,
          resolvers ++= repos
        )
  )

}
