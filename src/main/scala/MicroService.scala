package template.microservice

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import api.Service
import com.typesafe.config.ConfigFactory
import scala.language.postfixOps

object MicroService extends App with Service {
  override implicit val system = ActorSystem()
  override implicit val executor = system.dispatcher
  override implicit val materializer = ActorMaterializer()

  override val config = ConfigFactory.load()
  override val logger = Logging(system, getClass)

  //Http().bind(interface = config.getString("http.interface"), port = config.getInt("http.port")).startHandlingWith(routes)

  lazy val host = config.getString("http.interface")
  lazy val port = config.getInt("http.port")

  val http = Http().bindAndHandle(
    routes,
    host,
    port
  )

  sys.addShutdownHook {
    system.terminate()
  }
}
