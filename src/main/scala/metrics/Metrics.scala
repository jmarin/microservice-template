package metrics

import java.time.{ Duration, Instant }

trait Metrics {

  def time[T](name: String, f: => T): T = {
    val start = Instant.now()
    val result = f
    val end = Instant.now()
    val duration = Duration.between(start, end)
    println(s"${name} took: ${duration.toMillis}ms")
    result
  }

}
