import sbt._

object Dependencies {

  object versions {

    val grpc          = scalapb.compiler.Version.grpcJavaVersion
    val scalaPb       = scalapb.compiler.Version.scalapbVersion
    val fs2           = "1.1.0-M1"
    val catsEffect    = "2.0.0"
    val minitest      = "2.6.0"

    val kindProjector = "0.10.3"
    val sbtProtoc     = "0.99.23"

  }


  // Compile

  val fs2            = "co.fs2"        %% "fs2-core"          % versions.fs2
  val catsEffect     = "org.typelevel" %% "cats-effect"       % versions.catsEffect
  val grpcCore       = "io.grpc"       %  "grpc-core"         % versions.grpc

  // Testing

  val catsEffectLaws = "org.typelevel" %% "cats-effect-laws"  % versions.catsEffect
  val grpcNetty      = "io.grpc"       %  "grpc-netty-shaded" % versions.grpc
  val minitest       = "io.monix"      %% "minitest"          % versions.minitest

  // Compiler & SBT Plugins

  val sbtProtoc       = "com.thesamet"         %  "sbt-protoc"     % versions.sbtProtoc
  val scalaPbCompiler = "com.thesamet.scalapb" %% "compilerplugin" % versions.scalaPb
  val kindProjector   = "org.typelevel"        %% "kind-projector" % versions.kindProjector cross CrossVersion.binary

}
