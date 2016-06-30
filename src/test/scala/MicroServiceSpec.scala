package template.microservice

import akka.event.Logging
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.ContentTypes.`application/json`
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import api.Service
import com.typesafe.config.Config
import com.typesafe.scalalogging.Logger
import org.scalatest.{ FlatSpec, MustMatchers }
import org.slf4j.LoggerFactory

class MicroServiceSpec extends FlatSpec with MustMatchers with ScalatestRouteTest with Service {
  override def testConfigSource = "akka.loglevel = WARNING"
  override def config: Config = testConfig

  override val logger = Logging(system, getClass)
  override lazy val log = Logger(LoggerFactory.getLogger("microservice-test"))

  "A Microservice" should "respond to health checks" in {
    Get("/") ~> routes ~> check {
      status mustBe StatusCodes.OK
      val r = responseAs[model.Status]
      r.status mustBe "OK"
    }
  }

}
