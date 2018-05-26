package ru.steklopod

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers.{ModeledCustomHeader, ModeledCustomHeaderCompanion}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, WordSpec}
import spray.json._

import scala.collection.mutable
import scala.concurrent.Future
import scala.util.Try

class TeapotApiSpec extends WordSpec with Matchers with ScalatestRouteTest with TeapotJsonSupport {

  private val testTeapot = Teapot(1, 2, 3, 4, Color.Red, None)
  private val testTeapotForInsert = Teapot(2, 2, 3, 4, Color.Red, None)

  object TestApi extends Api {
    override val teapotRepository: TeapotRepository = new TeapotRepository {
      private val teapots = new mutable.HashMap[Long, Teapot]
      teapots += 1L -> testTeapot
      override def createTeapot(teapot: Teapot): Future[Unit] = Future.successful(teapots += (teapot.id -> teapot))

      override def getTeapot(id: Long): Future[Option[Teapot]] = Future.successful(teapots.get(id))
    }
  }

  object auth extends ModeledCustomHeaderCompanion[auth] {
    override val name                 = "auth"
    override def parse(value: String) = Try(new auth(value))
  }

  final class auth(override val value: String) extends ModeledCustomHeader[auth] {
    override def companion         = auth
    override def renderInRequests  = true
    override def renderInResponses = false
  }

  "The service" should {

    "return a teapot for GET requests to the /teapot/:id path" in {
      Get("/teapot/1") ~> auth("123") ~> TestApi.route ~> check {
        status === StatusCodes.ImATeapot
        responseAs[String].parseJson.convertTo[Teapot] shouldEqual testTeapot
      }
    }

    "return an error for GET requests without 'auth' header to the /teapot/:id path" in {
      Get("/teapot/1") ~> TestApi.route ~> check {
        status === StatusCodes.Unauthorized
        responseAs[String] shouldEqual "Authentication is possible but has failed or not yet been provided."
      }
    }

    "return an error for GET requests with invalid 'auth' header to the /teapot/:id path" in {
      Get("/teapot/1") ~> auth("321") ~> TestApi.route ~> check {
        status === StatusCodes.Unauthorized
        responseAs[String] shouldEqual "Authentication is possible but has failed or not yet been provided."
      }
    }

    "return an error for GET requests to the /teapot/:id path for not existed id" in {
      Get("/teapot/2") ~> TestApi.route ~> check {
        status === StatusCodes.NotFound
      }
    }

    "return OK for POST requests to the /teapot/ path" in {
      Post("/teapot", testTeapot) ~> auth("123") ~> TestApi.route ~> check {
        status === StatusCodes.OK
      }
    }
  }
}