#!/bin/bash

declare -a images=(
  "postgres:14"
)

for image in "${images[@]}"; do
  # https://stackoverflow.com/a/32074098
  docker rm $(docker stop $(docker ps -a -q --filter ancestor=$image --format="{{.ID}}")) || true
done
