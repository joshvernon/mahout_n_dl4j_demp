package org.rawkintrevo.stratademo

/**
  * Created by rawkintrevo on 3/14/17.
  */
trait Timed {

  def time[R](block: => R): Tuple2[Long, R] = {
    val t0 = System.nanoTime()
    val result = block    // call-by-name
    val t1 = System.nanoTime()
    println("Elapsed time: " + (t1 - t0) + "ns")
    (t1 - t0, result)
  }

  def doStuff(): Unit

  def main(args: Array[String]): Unit = {

    val times = new Array[Double](2)
    for (i <- 0 until times.length) {
      times(i) = time { doStuff() }._1 * 1E-9
    }

    val avgTime = times.sum / times.length
    println(s"Ran example ${times.length} iterations ")
    times.foreach(println)
    println(s"Average Time: $avgTime seconds" )
  }


}
