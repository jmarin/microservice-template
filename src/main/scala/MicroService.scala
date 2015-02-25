package template.microservice

import java.util.Calendar
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

case class Status(status: String, time: String)

trait JsonProtocol extends DefaultJsonProtocol {
  implicit val statusFormat = jsonFormat2(Status.apply)
}

trait Service extends JsonProtocol {
  implicit val system: ActorSystem
  implicit def executor: ExecutionContextExecutor
  implicit val materializer: ActorFlowMaterializer

  def config: Config
  val logger: LoggingAdapter

  val routes = {
    path("status") {
      get {
        compressResponseIfRequested() {
          complete {
            val now = Calendar.getInstance().getTime()
            ToResponseMarshallable(Status("OK", now.toString))
          }
        }
      }
    }
  }
}

object MicroService extends App with Service {
  override implicit val system = ActorSystem()
  override implicit val executor = system.dispatcher
  override implicit val materializer = ActorFlowMaterializer()

  override val config = ConfigFactory.load()
  override val logger = Logging(system, getClass)

  Http().bind(interface = config.getString("http.interface"), port = config.getInt("http.port")).startHandlingWith(routes)

}
