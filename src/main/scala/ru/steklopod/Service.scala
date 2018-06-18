package ru.steklopod

import java.util.concurrent.TimeUnit._

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import ru.steklopod.api.{GameApi, PlayerApi}
import ru.steklopod.repositories.PlayerDb.deleteOldSessions
import ru.steklopod.repositories._

import scala.concurrent.duration.Duration
import scala.io.StdIn

object WebServer extends GameApi with PlayerApi{

  def main(args: Array[String]) {

    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher

    val bindingFuture = Http().bindAndHandle(route ~ routeUser ~ routeDebug, "localhost", 8080)

    val scheduler = system.scheduler
    scheduler.schedule(
      initialDelay = Duration(5, MINUTES),
      interval = Duration(1, MINUTES),
      runnable = deleteOldSessions)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }

  override val gameRepository: GameRepository = DBGameRepository
  override val playerRepository: PlayerRepository = DBPlayerRepository
}
