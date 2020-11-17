addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.9.5")
addSbtPlugin("com.jsuereth" % "sbt-pgp" % "2.0.1")
addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "1.0.0")

addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.10.0")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.2")
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.5.1")

addSbtPlugin("com.typesafe" % "sbt-mima-plugin" % "0.8.1")

addSbtPlugin("io.github.davidgregory084" % "sbt-tpolecat" % "0.1.15")
addSbtPlugin("com.codecommit" % "sbt-github-actions" % "0.9.5")

addSbtPlugin("com.thesamet" % "sbt-protoc-gen-project" % "0.1.4")
libraryDependencies += "com.thesamet.scalapb" %% "compilerplugin" % "0.10.9"
