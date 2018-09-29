name := "gh4s"

scalaVersion := "2.12.6"
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
  if (priorTo2_13(scalaVersion.value))
    "-Ypartial-unification"
  else
    "-Ymacro-annotations"
}

lazy val circeVersion = "0.10.0"
lazy val sttpVersion  = "1.3.3"

libraryDependencies ++= Seq(
  // cats
  "org.typelevel" %% "cats-core"   % "1.4.0",
  "org.typelevel" %% "cats-effect" % "1.0.0",
  // circe
  "io.circe" %% "circe-core"           % circeVersion,
  "io.circe" %% "circe-generic"        % circeVersion,
  "io.circe" %% "circe-generic-extras" % circeVersion,
  "io.circe" %% "circe-parser"         % circeVersion,
  // sttp
  "com.softwaremill.sttp" %% "core"  % sttpVersion,
  "com.softwaremill.sttp" %% "circe" % sttpVersion,
  "com.softwaremill.sttp" %% "monix" % sttpVersion,
  // Testing
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
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
