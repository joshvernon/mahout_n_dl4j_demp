#!/usr/bin/env bash


echo "Downloading Spark 1.6.3"
wget http://d3kbcqa49mib13.cloudfront.net/spark-1.6.3-bin-hadoop2.6.tgz
tar -xzf spark-1.6.3-bin-hadoop2.6.tgz
export SPARK_HOME=$PWD/spark-1.6.3-bin-hadoop2.6
export DEMO_HOME=$PWD

#echo "Starting Spark"
$SPARK_HOME/sbin/start-all.sh

export MASTER=spark://$HOSTNAME:7077

bin/run_examples.sh >> output.txt

