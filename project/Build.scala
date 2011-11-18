import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "plaste-scala"
    val appVersion      = "1.0"

    val appDependencies = Seq(
      "pircbot" % "pircbot" % "1.5.0"
    )

    val main = PlayProject(appName, appVersion, appDependencies).settings(defaultScalaSettings:_*).settings(
      // Add your own project settings here      
    )

}
