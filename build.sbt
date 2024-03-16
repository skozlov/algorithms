ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"

ThisBuild / scalacOptions ++= Seq(
  "-encoding",
  "utf8",
  "--release:17",
  "-deprecation",
  "-Xfatal-warnings",
)

lazy val root = (project in file("."))
  .settings(
    name := "algorithms",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.19" % Test
    ),
  )

commands ++= Seq(
  Command.command("build") { state =>
    "scalafmtCheckAll" ::
      "scalafmtSbtCheck" ::
      "test" ::
      state
  },
  Command.command("rebuild") { state =>
    "clean" :: "build" :: state
  },
)
