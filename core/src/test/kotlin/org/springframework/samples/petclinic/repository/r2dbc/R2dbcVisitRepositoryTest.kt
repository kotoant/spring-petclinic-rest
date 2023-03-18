package org.springframework.samples.petclinic.repository.r2dbc

import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.samples.petclinic.repository.jdbc.JdbcVisitRepositoryTest.Companion.assertVisit1
import org.springframework.samples.petclinic.test.BaseTest
import org.springframework.test.context.ActiveProfiles
import reactor.test.StepVerifier
import java.time.LocalDate

private val log = KotlinLogging.logger {}

@ActiveProfiles("r2dbc")
class R2dbcVisitRepositoryTest : BaseTest() {
    @Autowired
    private lateinit var repository: R2dbcVisitRepository

    @Test
    fun fetchOneById() {
        StepVerifier.create(repository.fetchOneById(1))
            .assertNext { visit ->
                log.info("visit: {}", visit)
                assertVisit1(visit)
            }
            .verifyComplete()
    }

    @Test
    fun insert() {
        runBlocking {
            val visit = repository.insert(visit(testPet.id)).awaitSingle()
            assertThat(visit.id).isGreaterThan(0)
            assertVisit(visit, visit(testPet.id))

            val fetched = repository.fetchOneById(visit.id).awaitSingle()
            assertVisit(fetched, visit(testPet.id), visit.id)
        }
    }

    @Test
    fun update() {
        runBlocking {
            // Given
            val inserted = repository.insert(visit(testPet.id)).awaitSingle()
            val visit = inserted.copy(date = LocalDate.of(2023, 3, 15), description = "descr")

            // When
            val updated = repository.update(visit).awaitSingle()

            // Then
            assertVisit(updated, visit)
            val fetched = repository.fetchOneById(inserted.id).awaitSingle()
            assertVisit(fetched, visit)
        }
    }
}
