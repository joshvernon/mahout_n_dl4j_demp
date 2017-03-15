
package org.rawkintrevo.stratademo.mahout

import org.apache.mahout.math._
import org.apache.mahout.math.scalabindings._
import org.apache.mahout.math.drm._
import org.apache.mahout.math.scalabindings.RLikeOps._
import org.apache.mahout.math.drm.RLikeDrmOps._
import org.apache.mahout.sparkbindings._
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.rawkintrevo.stratademo.Timed


object Simple extends Timed {

  def doStuff(): Unit ={

    val conf = new SparkConf().setAppName("Simple mahout")
      .set("spark.kryo.referenceTracking", "false")

    val sc = new SparkContext(conf)
    implicit val sdc: org.apache.mahout.sparkbindings.SparkDistributedContext = sc2sdc(sc)

    val drmData = drmParallelize(dense(
      (2, 2, 10.5, 10, 29.509541),  // Apple Cinnamon Cheerios
      (1, 2, 12,   12, 18.042851),  // Cap'n'Crunch
      (1, 1, 12,   13, 22.736446),  // Cocoa Puffs
      (2, 1, 11,   13, 32.207582),  // Froot Loops
      (1, 2, 12,   11, 21.871292),  // Honey Graham Ohs
      (2, 1, 16,   8,  36.187559),  // Wheaties Honey Gold
      (6, 2, 17,   1,  50.764999),  // Cheerios
      (3, 2, 13,   7,  40.400208),  // Clusters
      (3, 3, 13,   4,  45.811716)), numPartitions = 2)

    drmData.collect(::, 0 until 4)

    val drmX = drmData(::, 0 until 4)
    val y = drmData.collect(::, 4)
    val drmXtX = drmX.t %*% drmX
    val drmXty = drmX.t %*% y


    val XtX = drmXtX.collect
    val Xty = drmXty.collect(::, 0)
    val beta = solve(XtX, Xty)
    sdc.stop()

    //println("The simple example worked- beta:")
    //println(beta)
  }

}
