package template.microservice

import java.net.InetAddress
import java.util.Calendar
import kamon.Kamon

import scala.concurrent.ExecutionContextExecutor
import scala.util.Properties
import spray.json.DefaultJsonProtocol
import akka.http.marshallers.sprayjson.SprayJsonSupport._
import akka.actor.ActorSystem
import akka.stream.ActorFlowMaterializer
import akka.event.{ LoggingAdapter, Logging }
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import akka.http.Http
import akka.http.marshalling.ToResponseMarshallable
import akka.http.server.Directives._
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

case class Status(status: String, time: String, hostname: String)

trait JsonProtocol extends DefaultJsonProtocol {
  implicit val statusFormat = jsonFormat3(Status.apply)
}

trait Service extends JsonProtocol {
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
            val statusCounter = Kamon.metrics.counter("status-counter")
            statusCounter.increment()
            val hostname = InetAddress.getLocalHost().getHostName()
            val now = Calendar.getInstance().getTime()
            log.info(s"Microservice running on ${hostname} - ${now}")
            ToResponseMarshallable(Status("OK", now.toString, hostname))
          }
        }
      }
    }
  }
}

object MicroService extends App with Service {
  Kamon.start()
  override implicit val system = ActorSystem()
  override implicit val executor = system.dispatcher
  override implicit val materializer = ActorFlowMaterializer()

  override val config = ConfigFactory.load()
  override val logger = Logging(system, getClass)

  Http().bind(interface = config.getString("http.interface"), port = config.getInt("http.port")).startHandlingWith(routes)

  sys.addShutdownHook {
    system.shutdown()
    Kamon.shutdown()
  }
}
