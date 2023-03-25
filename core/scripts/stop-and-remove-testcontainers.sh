#!/bin/bash

declare -a labels=(
  # keep in sync with org.springframework.samples.petclinic.test.BaseTestcontainersTest.POSTGRESQL_CONTAINER and core/scripts/calc-local-docker-env.sh:
  "org.springframework.samples.petclinic.testcontainers.postgres=petclinic"
)

for label in "${labels[@]}"; do
  # https://stackoverflow.com/a/32074098
  docker rm $(docker stop $(docker ps -a -q --filter "label=$label" --format="{{.ID}}")) || true
done
