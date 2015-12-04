name := "prog1p3"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies += "org.scala-lang" % "scala-swing" % "2.11+"

scalacOptions ++= Seq("-deprecation", "-unchecked", "-Xlint", "-Xdisable-assertions")

scalacOptions in (Compile, doc) ++= Seq("-doc-root-content", baseDirectory.value+"/root-doc.txt")
