name := "Scala coursework"

version := "0.1"

scalaVersion := "2.12.5"


libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "org.scalacheck" %% "scalacheck" % "1.13.5" % "test",
  "com.storm-enroute" %% "scalameter" % "0.9" % "test" excludeAll ExclusionRule(organization = "org.mongodb"),
  "com.typesafe.akka" %% "akka-actor" % "2.5.11",
  "com.typesafe.akka" % "akka-stream_2.12" % "2.5.11",
  "com.typesafe.akka" %% "akka-slf4j" % "2.5.11",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.11" % "test",
  "com.typesafe.akka" %% "akka-http" % "10.1.0",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.0" % "test",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.0",
  "io.spray" %% "spray-json" % "1.3.4",
  "com.h2database" % "h2" % "1.4.197",
  "org.mariadb.jdbc" % "mariadb-java-client" % "2.2.3",
  "org.postgresql" % "postgresql" % "42.2.2",
  "com.typesafe.slick" %% "slick" % "3.2.3",
  "org.tpolecat" %% "doobie-core" % "0.5.1",
  "org.scalikejdbc" %% "scalikejdbc" % "3.2.1",
  "org.scalikejdbc" %% "scalikejdbc-config" % "3.2.1",
  "org.scalikejdbc" %% "scalikejdbc-test" % "3.2.1" % "test",
  "ch.qos.logback" % "logback-classic" % "1.2.1",
  "com.typesafe.play" %% "anorm" % "2.5.3",
  "org.mongodb" %% "casbah" % "3.1.1",
  "com.github.salat" %% "salat" % "1.11.2",
  "org.scala-lang.modules" %% "scala-async" % "0.9.7",
  "com.github.fakemongo" % "fongo" % "2.1.0",
  "com.chuusai" %% "shapeless" % "2.3.3",
  "com.tsukaby" %% "bean-validation-scala" % "0.4.2",
  "com.h2database" % "h2" % "1.4.197" % Test
)

testFrameworks += new TestFramework("org.scalameter.ScalaMeterFramework")

parallelExecution in Test := false

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)
