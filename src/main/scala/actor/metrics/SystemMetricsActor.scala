package actor.metrics

import akka.actor.{ Actor, Props }
import kamon.Kamon
import kamon.metric.EntitySnapshot

case class JVMMetrics()

object SystemMetricsActor {
  def props: Props = {
    Props(new SystemMetricsActor)
  }
}

class SystemMetricsActor extends Actor {
  lazy val collectionContext = Kamon.metrics.buildDefaultCollectionContext

  override def receive: Receive = {
    case JVMMetrics =>
      val heapMetrics = takeSnapshotOf("heap-memory", "system-metric")
      heapMetrics.gauge("heap-used")
      heapMetrics.gauge("heap-max")
      heapMetrics.gauge("heap-committed")
  }

  private def takeSnapshotOf(name: String, category: String): EntitySnapshot = {
    val recorder = Kamon.metrics.find(name, category).get
    recorder.collect(collectionContext)
  }
}
