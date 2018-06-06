package ru.steklopod

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import ru.steklopod.repositories.{DBGameRepository, DBPlayerRepository, GameRepository, PlayerRepository}
import akka.http.scaladsl.server.Directives._

import scala.io.StdIn

object WebServer extends Api {

  def main(args: Array[String]) {

    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher

    val bindingFuture = Http().bindAndHandle(route ~ routeUser, "localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }

  override val gameRepository: GameRepository = DBGameRepository
  override val playerRepository: PlayerRepository = DBPlayerRepository

}