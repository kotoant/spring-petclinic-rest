#!/bin/bash

echo spring.config.location=core/src/test/resources/application.yml > .env
echo -n 'LOCAL_POSTGRES_PORT=' >> .env
echo $(docker ps -a -q -f ancestor='postgres:14' | xargs -I{} docker port {} 5432 | cut -c9-) >> .env
echo 'DB_R2DBC_URL=r2dbc:postgresql://localhost:${LOCAL_POSTGRES_PORT}/petclinic' >> .env
echo 'DB_JDBC_URL=jdbc:postgresql://localhost:${LOCAL_POSTGRES_PORT}/petclinic?loggerLevel=OFF' >> .env
echo DB_USERNAME=petclinic >> .env
echo DB_PASSWORD=petclinic >> .env
