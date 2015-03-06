package template.microservice

import akka.event.Logging
import akka.http.model.StatusCodes._
import akka.http.model.MediaTypes._
import akka.http.testkit.ScalatestRouteTest
import com.typesafe.config.Config
import com.typesafe.scalalogging.Logger
import org.scalatest.{ FlatSpec, MustMatchers }
import org.slf4j.LoggerFactory
import akka.http.marshallers.sprayjson.SprayJsonSupport._

class MicroServiceSpec extends FlatSpec with MustMatchers with ScalatestRouteTest with Service {
  override def testConfigSource = "akka.loglevel = WARNING"
  override def config: Config = testConfig

  override val logger = Logging(system, getClass)
  override lazy val log = Logger(LoggerFactory.getLogger("microservice-test"))

  "A Microservice" should "respond to health checks" in {
    Get("/status") ~> routes ~> check {
      status mustBe OK
      contentType.mediaType mustBe `application/json`
      val r = responseAs[Status]
      r.status mustBe "OK"
    }
  }

}
