name := "gh4s"

scalaVersion := "2.12.7"
crossScalaVersions ++= Seq("2.11.12" /*, "2.13.0-M4"*/ )

scalafmtOnCompile := true

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
)

scalacOptions += {
  if (priorTo2_13(scalaVersion.value)) "-Ypartial-unification" else "-Ymacro-annotations"
}

lazy val circeVersion  = "0.11.1"
lazy val http4sVersion = "0.20.0-M1"

libraryDependencies ++= Seq(
  // cats
  "org.typelevel" %% "cats-core"   % "1.6.0",
  "org.typelevel" %% "cats-effect" % "1.2.0",
  // circe
  "io.circe" %% "circe-core"           % circeVersion,
  "io.circe" %% "circe-generic"        % circeVersion,
  "io.circe" %% "circe-generic-extras" % circeVersion,
  // http4s
  "org.http4s" %% "http4s-circe"  % http4sVersion,
  "org.http4s" %% "http4s-client" % http4sVersion,
  // Testing
  "org.http4s"    %% "http4s-dsl" % http4sVersion % "test",
  "org.scalatest" %% "scalatest"  % "3.0.5"       % "test",
)

libraryDependencies ++= {
  if (priorTo2_13(scalaVersion.value))
    Seq(compilerPlugin(("org.scalamacros" % "paradise" % "2.1.1").cross(CrossVersion.full)))
  else
    Seq.empty
}

def priorTo2_13(scalaVersion: String): Boolean =
  CrossVersion.partialVersion(scalaVersion) match {
    case Some((2, minor)) if minor < 13 => true
    case _                              => false
  }
