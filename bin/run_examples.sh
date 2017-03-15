#!/usr/bin/env bash


echo "Downloading Eigenfaces Data"
mkdir data
cd data
wget http://vis-www.cs.umass.edu/lfw/lfw-deepfunneled.tgz
tar -xzf lfw-deepfunneled.tgz
cd ..

echo "Simple Example - Non Accelerated"
$SPARK_HOME/bin/spark-submit \
              --class org.rawkintrevo.stratademo.mahout.Simple \
              --master $MASTER \
              --conf spark.kryo.referenceTracking=false \
              --conf spark.kryo.registrator=org.apache.mahout.sparkbindings.io.MahoutKryoRegistrator \
              --conf spark.kryoserializer.buffer=32k \
              --conf spark.kryoserializer.buffer.max=600m \
              --conf spark.serializer=org.apache.spark.serializer.KryoSerializer \
              $DEMO_HOME/libs/strata-demo-1.0-SNAPSHOT-nogpu.jar



echo "Eigenfaces Example - Non Accelerated"
$SPARK_HOME/bin/spark-submit \
              --class org.rawkintrevo.stratademo.mahout.Eigenfaces \
              --master $MASTER \
              --conf spark.kryo.referenceTracking=false \
              --conf spark.kryo.registrator=org.apache.mahout.sparkbindings.io.MahoutKryoRegistrator \
              --conf spark.kryoserializer.buffer=32k \
              --conf spark.kryoserializer.buffer.max=600m \
              --conf spark.serializer=org.apache.spark.serializer.KryoSerializer \
              --conf spark.executor.memory  4g \
              $DEMO_HOME/libs/strata-demo-1.0-SNAPSHOT-nogpu.jar

echo "MLP Example - Non Accelerated"
$SPARK_HOME/bin/spark-submit \
              --class org.rawkintrevo.stratademo.dl4j.mlp \
              --master $MASTER \
              --conf spark.kryo.referenceTracking=false \
              --conf spark.kryo.registrator=org.nd4j.Nd4jRegistrator \
              --conf spark.executor.memory  4g \
              --conf spark.serializer=org.apache.spark.serializer.KryoSerializer \
              $DEMO_HOME/libs/strata-demo-1.0-SNAPSHOT-nogpu.jar


echo "Simple Example - Accelerated"
$SPARK_HOME/bin/spark-submit \
              --class org.rawkintrevo.stratademo.mahout.Simple \
              --master $MASTER \
              --conf spark.kryo.referenceTracking=false \
              --conf spark.kryo.registrator=org.apache.mahout.sparkbindings.io.MahoutKryoRegistrator \
              --conf spark.kryoserializer.buffer=32k \
              --conf spark.kryoserializer.buffer.max=600m \
              --conf spark.serializer=org.apache.spark.serializer.KryoSerializer \
              $DEMO_HOME/libs/strata-demo-1.0-SNAPSHOT-gpu.jar



echo "Eigenfaces Example - Accelerated"
$SPARK_HOME/bin/spark-submit \
              --class org.rawkintrevo.stratademo.mahout.Eigenfaces \
              --master $MASTER \
              --conf spark.kryo.referenceTracking=false \
              --conf spark.kryo.registrator=org.apache.mahout.sparkbindings.io.MahoutKryoRegistrator \
              --conf spark.kryoserializer.buffer=32k \
              --conf spark.kryoserializer.buffer.max=600m \
              --conf spark.executor.memory  4g \
              --conf spark.serializer=org.apache.spark.serializer.KryoSerializer \
              $DEMO_HOME/libs/strata-demo-1.0-SNAPSHOT-gpu.jar

echo "MLP Example - Accelerated"
$SPARK_HOME/bin/spark-submit \
              --class org.rawkintrevo.stratademo.dl4j.mlp \
              --master $MASTER \
              --conf spark.kryo.referenceTracking=false \
              --conf spark.kryo.registrator=org.nd4j.Nd4jRegistrator \
              --conf spark.serializer=org.apache.spark.serializer.KryoSerializer \
              --conf spark.executor.memory  4g \
              $DEMO_HOME/libs/strata-demo-1.0-SNAPSHOT-gpu.jar

