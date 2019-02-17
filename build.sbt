name := "cocomponent"

version := "0.1"

scalaVersion := "2.12.8"

scalacOptions += "-Ypartial-unification"

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.6")

val catsVersion = "1.5.0"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-macros" % catsVersion,
  "org.typelevel" %% "cats-kernel" % catsVersion,
  "org.typelevel" %% "cats-core" % catsVersion,
  "org.typelevel" %% "cats-laws" % catsVersion,
  "org.typelevel" %% "cats-free" % catsVersion,
  "org.typelevel" %% "cats-testkit" % catsVersion,
  "org.typelevel" %% "cats-effect" % "1.2.0",
  "org.json4s" %% "json4s-native" % "3.6.4",
  "co.fs2" %% "fs2-core" % "1.0.1",
  "joda-time" % "joda-time" % "2.8.2"
//  "io.circe" %% "circe-core" % "0.10.0",
)

