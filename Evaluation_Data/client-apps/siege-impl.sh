#! /bin/bash

client_projects_path=(
  "ZingClient"
)

current_dir="$PWD"
csv_path="$current_dir/vulnerabilities.csv"
siege_jar_path="$current_dir/siege-1.0.7-SNAPSHOT.jar"

current_dir="$PWD"

function work() {
  cd "$1" || exit
  java -jar $siege_jar_path -vulnerabilities $csv_path -budget 120 -librariesPath target/dependency -export
}

for dir in "${client_projects_path[@]}"; do
  project_dir="$current_dir/$dir"
  work "$project_dir" &
done

wait

