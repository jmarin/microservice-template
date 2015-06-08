package template.microservice

import actor.metrics.{ JVMMetrics, SystemMetricsActor }
import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.Http
import akka.stream.ActorFlowMaterializer
import api.Service
import com.typesafe.config.ConfigFactory
import kamon.Kamon
import scala.concurrent.duration._
import scala.language.postfixOps

object MicroService extends App with Service {
  Kamon.start()
  override implicit val system = ActorSystem()
  override implicit val executor = system.dispatcher
  override implicit val materializer = ActorFlowMaterializer()

  override val config = ConfigFactory.load()
  override val logger = Logging(system, getClass)

  Http().bind(interface = config.getString("http.interface"), port = config.getInt("http.port")).startHandlingWith(routes)

  val systemActor = system.actorOf(SystemMetricsActor.props)
  system.scheduler.schedule(1000 milliseconds, 1000 milliseconds, systemActor, JVMMetrics)

  sys.addShutdownHook {
    system.shutdown()
    Kamon.shutdown()
  }
}
