#!/bin/bash

# keep in sync with org.springframework.samples.petclinic.test.BaseTestcontainersTest.POSTGRESQL_CONTAINER and core/scripts/stop-and-remove-testcontainers.sh:
DB_PORT=$(docker ps -a -q -f "label=org.springframework.samples.petclinic.testcontainers.postgres=petclinic" | xargs -I{} docker port {} 5432 | cut -c9-)

echo -n 'DB_HOST=localhost:' >local.env
echo "$DB_PORT" >>local.env

echo -n 'DB_HOST=docker.for.mac.host.internal:' >docker.env
echo "$DB_PORT" >>docker.env
