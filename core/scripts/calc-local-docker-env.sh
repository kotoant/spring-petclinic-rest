#!/bin/bash

echo -n 'DB_HOST=localhost:' >local.env
# keep in sync with org.springframework.samples.petclinic.test.BaseTestcontainersTest.POSTGRESQL_CONTAINER and core/scripts/stop-and-remove-testcontainers.sh:
echo $(docker ps -a -q -f "label=org.springframework.samples.petclinic.testcontainers.postgres=petclinic" | xargs -I{} docker port {} 5432 | cut -c9-) >>local.env
