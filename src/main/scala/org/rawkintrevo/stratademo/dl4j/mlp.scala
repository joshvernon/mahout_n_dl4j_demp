package org.rawkintrevo.stratademo.dl4j

import org.apache.spark.{SparkConf, SparkContext}
import org.rawkintrevo.stratademo.Timed
/**
  * Created by rawkintrevo on 3/14/17.
  */
object mlp extends Timed{
  override def doStuff() = {
    /********* Paragraph 0: Stuff Jupyter Did for us ********************************************/
    val sparkConf = new SparkConf().setAppName("MLP DL4j")

    val sc = new SparkContext(sparkConf)

    /********* Paragraph 1 **********************************************************************/
    import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator;
    import org.deeplearning4j.eval.Evaluation;
    import org.deeplearning4j.nn.api.OptimizationAlgorithm;
    import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
    import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
    import org.deeplearning4j.nn.conf.Updater;
    import org.deeplearning4j.nn.conf.layers.DenseLayer;
    import org.deeplearning4j.nn.conf.layers.OutputLayer;
    import org.deeplearning4j.nn.weights.WeightInit;
    import org.deeplearning4j.spark.api.TrainingMaster;
    import org.deeplearning4j.spark.impl.multilayer.SparkDl4jMultiLayer;
    import org.deeplearning4j.spark.impl.paramavg.ParameterAveragingTrainingMaster;
    import org.nd4j.linalg.activations.Activation;
    import org.nd4j.linalg.dataset.DataSet;
    import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
    import org.nd4j.linalg.lossfunctions.LossFunctions;

    println("Packages Loaded...")
    val batchSizePerWorker = 16
    val iterTrain = new MnistDataSetIterator(batchSizePerWorker, true, 12345);
    val iterTest = new MnistDataSetIterator(batchSizePerWorker, true, 12345);
    val trainDataList = new scala.collection.mutable.ArrayBuffer[org.nd4j.linalg.dataset.DataSet]
    val testDataList = new scala.collection.mutable.ArrayBuffer[org.nd4j.linalg.dataset.DataSet]
    println("Creating Data Sets...")
    while (iterTrain.hasNext()) {
      trainDataList += iterTrain.next();
    }
    while (iterTest.hasNext()) {
      testDataList += iterTest.next();
    }
    println("Parallelizing Datasets")

    val trainData = sc.parallelize(trainDataList);
    val testData = sc.parallelize(testDataList);


    println(s"training: ${trainData.count()} obs, testsing: ${testData.count()} obs")
    // bug can't split line up...
    val conf = new NeuralNetConfiguration.Builder().seed(12345).optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT).iterations(1).activation(Activation.LEAKYRELU).weightInit(WeightInit.XAVIER).learningRate(0.02).updater(Updater.NESTEROVS).momentum(0.9).regularization(true).l2(1e-4).list().layer(0, new DenseLayer.Builder().nIn(28 * 28).nOut(500).build()).layer(1, new DenseLayer.Builder().nIn(500).nOut(100).build()).layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD).activation(Activation.SOFTMAX).nIn(100).nOut(10).build()).pretrain(false).backprop(true).build()

    //Each DataSet object: contains (by default) 32 examples
    //Async prefetching: 2 examples per worker
    val numEpochs = 5
    val tm = new ParameterAveragingTrainingMaster.Builder(batchSizePerWorker).averagingFrequency(5).workerPrefetchNumBatches(2).batchSizePerWorker(batchSizePerWorker).build();

    //Create the Spark network
    val sparkNet = new SparkDl4jMultiLayer(sc, conf, tm);

    //Execute training:
    for (i <- 0 to numEpochs) {
      sparkNet.fit(trainData);
      println(s"Completed Epoch ${i}");
    }

    //Perform evaluation (distributed)
    val evaluation = sparkNet.evaluate(testData);
    println("***** Evaluation *****");
    println(evaluation.stats());

    //Delete the temp training files, now that we are done with them
    tm.deleteTempFiles(sc);

    println("***** Example Complete *****");

    sc.stop()
  }

}
