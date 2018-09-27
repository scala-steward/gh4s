import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}
import org.scalafmt.sbt.ScalafmtPlugin.scalafmtConfigSettings

def priorTo2_13(scalaVersion: String): Boolean =
  CrossVersion.partialVersion(scalaVersion) match {
    case Some((2, minor)) if minor < 13 => true
    case _                              => false
  }

lazy val crossCompilationSettings = Seq(
  scalacOptions += {
    if (priorTo2_13(scalaVersion.value))
      "-Ypartial-unification"
    else
      "-Ymacro-annotations"
  },
  libraryDependencies ++= {
    if (priorTo2_13(scalaVersion.value))
      Seq(compilerPlugin(("org.scalamacros" % "paradise" % "2.1.1").cross(CrossVersion.full)))
    else
      Seq.empty
  }
)

lazy val sharedSettings = crossCompilationSettings ++ Seq(
  scalaVersion := "2.12.6",
  crossScalaVersions ++= Seq("2.11.12" /*, "2.13.0-M4"*/ ),
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding",
    "UTF-8",
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-unchecked",
    "-Xfatal-warnings",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Xfuture",
    "-Ywarn-unused-import"
  ),
  scalafmtOnCompile := true,
  wartremoverErrors in (Compile, compile) := Warts.allBut(
    Wart.DefaultArguments,
    Wart.ExplicitImplicitTypes,
    Wart.Nothing,
    Wart.PublicInference,
  ),
  wartremoverErrors in (Test, compile) := (wartremoverErrors in (Compile, compile)).value.diff(
    Seq(
      Wart.Any,
      Wart.NonUnitStatements
    )),
) ++ inConfig(IntegrationTest)(scalafmtConfigSettings)

lazy val root = project.in(file(".")).aggregate(gh4sJVM, gh4sJS)

lazy val gh4s = crossProject(JVMPlatform, JSPlatform)
  .withoutSuffixFor(JVMPlatform)
  .crossType(CrossType.Full)
  .settings(sharedSettings)
  .configs(IntegrationTest)
  .settings(
    libraryDependencies ++= Seq(
      // cats
      "org.typelevel" %%% "cats-core"   % "1.4.0",
      "org.typelevel" %%% "cats-effect" % "1.0.0",
      // circe
      "io.circe" %%% "circe-core"           % circeVersion,
      "io.circe" %%% "circe-generic"        % circeVersion,
      "io.circe" %%% "circe-generic-extras" % circeVersion,
      "io.circe" %%% "circe-parser"         % circeVersion,
      // sttp
      "com.softwaremill.sttp" %% "core"  % sttpVersion,
      "com.softwaremill.sttp" %% "circe" % sttpVersion,
      // Testing
      "org.scalatest" %%% "scalatest" % "3.0.5" % "test"
    )
  )
  .jvmSettings(
    libraryDependencies += "com.softwaremill.sttp" %% "okhttp-backend-monix" % sttpVersion
  )

lazy val gh4sJVM = gh4s.jvm
lazy val gh4sJS  = gh4s.js

lazy val circeVersion = "0.10.0"
lazy val sttpVersion  = "1.3.3"
