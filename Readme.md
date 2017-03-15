### Build a no-Gpu support version

`mvn clean package -Pnogpu -Djar.tag=nogpu`

### Build a no-Gpu support version

`mvn clean package -Pgpu -Djar.tag=gpu`

### Run Examples on GPU Enabled Cluster

Create a tarbal with `bin` and `libs` (containing jars- eg. build first, dummy)

download tarball and unzip.

`cd tarball_dir`
`chmod +x bin/run_examples.sh`

`export MASTER=spark://localhost:7077`  or wherever

`bin/run_examples.sh >> output.txt`

`ssh nimbix@youraddress`

`wget https://github.com/rawkintrevo/mahout_n_dl4j_demp/releases/download/stratademo/stratademo.tar.gz`
`tar -xzf *`
`cd *`
`chmod +x bin/*`
`bin/full-demo.sh`

From your machine copy the output of the timers:
`scp nimbix@remotehost.edu:foobar.txt ./output.txt`