package org.springframework.samples.petclinic.test

import mu.KotlinLogging
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.PostgreSQLContainer.POSTGRESQL_PORT
import org.testcontainers.utility.DockerImageName

import java.time.Duration

private val log = KotlinLogging.logger {}

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BaseTestcontainersTest {
    companion object {
        private const val IMAGE_VERSION = "postgres:14"
        private val POSTGRESQL_CONTAINER: PostgreSQLContainer<*> =
            PostgreSQLContainer(DockerImageName.parse(IMAGE_VERSION).asCompatibleSubstituteFor("postgres"))
                .withReuse(true)
                // keep in sync with 'core/scripts/calc-local-docker-env.sh' and 'core/scripts/stop-and-remove-testcontainers.sh'
                .withLabel("org.springframework.samples.petclinic.testcontainers.postgres", "petclinic")
                .withStartupTimeout(Duration.ofMinutes(5))
                .withUsername("petclinic")
                .withDatabaseName("petclinic")
                .withPassword("petclinic")

        init {
            System.setProperty("jdk.httpclient.keepalive.timeout", "10")
            POSTGRESQL_CONTAINER.start()
            val port = POSTGRESQL_CONTAINER.getMappedPort(POSTGRESQL_PORT)
            log.info("Postgresql container started. Address: {}, port: {}", POSTGRESQL_CONTAINER.host, port)

            System.setProperty("DB_HOST", "${POSTGRESQL_CONTAINER.host}:$port")
            System.setProperty("DB_NAME", POSTGRESQL_CONTAINER.databaseName)
            System.setProperty("DB_USERNAME", POSTGRESQL_CONTAINER.username)
            System.setProperty("DB_PASSWORD", POSTGRESQL_CONTAINER.password)
        }
    }
}
