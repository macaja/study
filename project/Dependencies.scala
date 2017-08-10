import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.3"
  lazy val monix = "io.monix" %% "monix" % "2.3.0"
  lazy val monixKafka = "io.monix" %% "monix-kafka-10" % "0.14"
  lazy val scalaPb = "com.trueaccord.scalapb" %% "scalapb-runtime" % com.trueaccord.scalapb.compiler.Version.scalapbVersion % "protobuf"
  lazy val scalapbGrpc = "com.trueaccord.scalapb" %% "scalapb-runtime-grpc" % com.trueaccord.scalapb.compiler.Version.scalapbVersion
  lazy val pureconfig = "com.github.pureconfig" %% "pureconfig" % "0.7.2"
  lazy val cats = "org.typelevel" %% "cats" % "0.9.0" withSources () withJavadoc ()
}
