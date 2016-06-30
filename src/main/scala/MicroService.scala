package template.microservice

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.Http
import akka.stream.ActorFlowMaterializer
import api.Service
import com.typesafe.config.ConfigFactory
import scala.language.postfixOps

object MicroService extends App with Service {
  override implicit val system = ActorSystem()
  override implicit val executor = system.dispatcher
  override implicit val materializer = ActorFlowMaterializer()

  override val config = ConfigFactory.load()
  override val logger = Logging(system, getClass)

  Http().bind(interface = config.getString("http.interface"), port = config.getInt("http.port")).startHandlingWith(routes)

  sys.addShutdownHook {
    system.shutdown()
  }
}
