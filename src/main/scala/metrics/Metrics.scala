package metrics

import java.time.{ Duration, Instant }

import kamon.Kamon

trait Metrics {

  def time[T](name: String, f: => T): T = {
    val gauge = Kamon.metrics.histogram("time")
    val start = Instant.now()
    val result = f
    val end = Instant.now()
    val duration = Duration.between(start, end)
    //println(s"${name} took: ${duration.toMillis}")
    result
  }

  def count[T](name: String, f: => T): T = {
    val statusCounter = Kamon.metrics.counter(name)
    statusCounter.increment()
    val result = f
    result
  }

  //  def measure[T](name: String, f: => T): T = {
  //    val gauge = Kamon.metrics.histogram(name)
  //    val statusCounter = Kamon.metrics.counter(name)
  //    statusCounter.increment()
  //    val result = f
  //    result
  //  }
}
