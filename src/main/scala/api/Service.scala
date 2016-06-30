package api

import java.net.InetAddress
import java.util.Calendar

import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import com.typesafe.config.Config
import com.typesafe.scalalogging.Logger
import metrics.Metrics
import model.Status
import org.slf4j.LoggerFactory
import protocol.JsonProtocol

import scala.concurrent.ExecutionContextExecutor

trait Service extends JsonProtocol with Metrics {
  implicit val system: ActorSystem
  implicit def executor: ExecutionContextExecutor
  implicit val materializer: ActorMaterializer

  def config: Config
  val logger: LoggingAdapter

  lazy val log = Logger(LoggerFactory.getLogger("microservice"))

  val routes = {
    path("") {
      get {
        encodeResponse {
          complete {
            time("status", serviceStatus)
          }
        }
      }
    }
  }

  def serviceStatus: ToResponseMarshallable = {
    val hostname = InetAddress.getLocalHost().getHostName()
    val now = Calendar.getInstance().getTime()
    log.info(s"Microservice running on ${hostname} - ${now}")
    ToResponseMarshallable(Status("OK", now.toString, hostname))
  }
}