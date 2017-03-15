### Build a no-Gpu support version

`mvn clean package -Pnogpu -Djar.tag=nogpu`

### Build a Gpu support version

`mvn clean package -Pgpu -Djar.tag=gpu`

### Run Examples on GPU Enabled Cluster

Create a tarbal with `bin` and `libs` (containing jars- eg. build first, dummy)

download tarball and unzip.

`cd tarball_dir`
`chmod +x bin/run_examples.sh`

`export MASTER=spark://localhost:7077`  or wherever

`bin/run_examples.sh >> output.txt`

