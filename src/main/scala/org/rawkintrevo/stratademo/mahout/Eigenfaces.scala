package org.rawkintrevo.stratademo.mahout

import org.apache.mahout.sparkbindings.sc2sdc
import org.apache.spark.{SparkConf, SparkContext}
import org.rawkintrevo.stratademo.Timed

/**
  * Created by rawkintrevo on 3/14/17.
  */
object Eigenfaces extends Timed {

  override def doStuff() = {
    /********* Paragraph 0: Stuff Jupyter Did for us ********************************************/
    val conf = new SparkConf().setAppName("Eigenfaces Mahout")

    val sc = new SparkContext(conf)

    /********* Paragraph 1 **********************************************************************/
    import org.apache.mahout.math._
    import org.apache.mahout.math.scalabindings._
    import org.apache.mahout.math.drm._
    import org.apache.mahout.math.scalabindings.RLikeOps._
    import org.apache.mahout.math.drm.RLikeDrmOps._
    import org.apache.mahout.sparkbindings._

    implicit val sdc: org.apache.mahout.sparkbindings.SparkDistributedContext = sc2sdc(sc)

    /********* Paragraph 2 **********************************************************************/
    import com.sksamuel.scrimage._
    import com.sksamuel.scrimage.filter.GrayscaleFilter

    // CALL OUT:  I changed the source to pull ALL Files "*3" → "*" //
    val imagesRDD = sc.binaryFiles(s"""file:///${sys.env("DEMO_HOME")}/data/lfw-deepfunneled/*/*.jpg""")
      .map(o => (new DenseVector( Image.apply(o._2.toArray)
      .filter(GrayscaleFilter)
      .pixels
      .map(p => p.toInt.toDouble / 10000000)),
      o._1.split("/").last.split("_").slice(0,2).mkString(" ")) ).zipWithIndex

    val preDRM:DrmRdd[Int] = imagesRDD.map(o => (o._2.toInt, o._1._1))

    val imagesDRM = drmWrap(rdd= preDRM).par(min = 2).checkpoint()

    println(s"Dataset: ${imagesDRM.nrow} images, ${imagesDRM.ncol} pixels per image")

    /********* Paragraph 3 **********************************************************************/
    import org.apache.mahout.math.algorithms.preprocessing._

    val scaler: MeanCenterModel = new MeanCenter().fit(imagesDRM)

    val smImages = scaler.transform(imagesDRM)

    smImages.checkpoint()
    /********* Paragraph 4 **********************************************************************/
    import org.apache.mahout.math._
    import decompositions._



    val(drmU, drmV, s) = dssvd(smImages, k= 100, p= 15, q = 0)
    /********* Paragraph 5 **********************************************************************/
    // Omitted bc I don't want to write output for speed tests- but you get the idea...

    sc.stop()
    /* we could ommit this if we were only running the job once- but since we're looping
    it over and over we need to stop each time we're done
     */

  }

}
