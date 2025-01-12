#! /bin/bash

script_dir="$(cd "$(dirname "$0")" && pwd)"
root_dir=$(dirname $script_dir)
jar_dir="$root_dir/magneto-engine/target/magneto-engine-1.0.jar"

java -jar -noverify $jar_dir \
-p xxxxxx \
-skipFuzz \
-output CVE-2022-45688__db \
-vul CVE-2022-45688
