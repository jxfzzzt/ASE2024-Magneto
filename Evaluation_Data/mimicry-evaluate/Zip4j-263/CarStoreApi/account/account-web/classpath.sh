#! /bin/bash

ROOT_DIR=`dirname $0`

cp="$ROOT_DIR/target/classes/"

for jar in $ROOT_DIR/target/dependency/*.jar; do
  cp="$cp:$jar"
done

cp="$cp:$ROOT_DIR/Zip4j-263-1.0.jar"

echo $cp