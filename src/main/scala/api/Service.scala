package api

import java.net.InetAddress
import java.util.Calendar
import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.http.marshalling.ToResponseMarshallable
import akka.http.marshallers.sprayjson.SprayJsonSupport._
import akka.http.server.Directives._
import akka.stream.ActorFlowMaterializer
import com.typesafe.config.Config
import com.typesafe.scalalogging.Logger
import kamon.Kamon
import metrics.Metrics
import model.Status
import org.slf4j.LoggerFactory
import protocol.JsonProtocol
import scala.concurrent.ExecutionContextExecutor

trait Service extends JsonProtocol with Metrics {
  implicit val system: ActorSystem
  implicit def executor: ExecutionContextExecutor
  implicit val materializer: ActorFlowMaterializer

  def config: Config
  val logger: LoggingAdapter

  lazy val log = Logger(LoggerFactory.getLogger("microservice"))

  val routes = {
    path("status") {
      get {
        compressResponseIfRequested() {
          complete {
            time("status", status)
          }
        }
      }
    }
  }

  def status: ToResponseMarshallable = {
    val statusCounter = Kamon.metrics.counter("status-counter")
    statusCounter.increment()
    val hostname = InetAddress.getLocalHost().getHostName()
    val now = Calendar.getInstance().getTime()
    log.info(s"Microservice running on ${hostname} - ${now}")
    ToResponseMarshallable(Status("OK", now.toString, hostname))
  }
}