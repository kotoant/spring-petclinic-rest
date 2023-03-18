package org.springframework.samples.petclinic.test

import mu.KotlinLogging
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.containers.PostgreSQLContainer
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
                .withStartupTimeout(Duration.ofMinutes(5))
                .withUsername("petclinic")
                .withDatabaseName("petclinic")
                .withPassword("petclinic")

        init {
            POSTGRESQL_CONTAINER.start()
            log.info(
                "Postgresql container started. Address: {}, port: {}",
                POSTGRESQL_CONTAINER.host,
                POSTGRESQL_CONTAINER.firstMappedPort
            )

            val r2dbcUrl = getR2dbcUrl()
            log.info("r2dbcUrl: {}", r2dbcUrl)
            System.setProperty("DB_R2DBC_URL", r2dbcUrl)

            val jdbcUrl = POSTGRESQL_CONTAINER.jdbcUrl
            log.info("jdbcUrl: {}", jdbcUrl)
            System.setProperty("DB_JDBC_URL", jdbcUrl)

            System.setProperty("DB_USERNAME", POSTGRESQL_CONTAINER.username)
            System.setProperty("DB_PASSWORD", POSTGRESQL_CONTAINER.password)
        }

        private fun getR2dbcUrl() =
            "r2dbc:postgresql://" +
                    POSTGRESQL_CONTAINER.host +
                    ":" +
                    POSTGRESQL_CONTAINER.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT) +
                    "/" +
                    POSTGRESQL_CONTAINER.databaseName
    }
}
