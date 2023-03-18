package org.springframework.samples.petclinic.repository.jdbc

import mu.KotlinLogging
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.samples.petclinic.model.Visit
import org.springframework.samples.petclinic.test.BaseTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate

private val log = KotlinLogging.logger {}

@ActiveProfiles("jdbc")
class JdbcVisitRepositoryTest : BaseTest() {
    @Autowired
    private lateinit var repository: JdbcVisitRepository

    @Test
    fun fetchOneById() {
        val visit: Visit = repository.fetchOneById(1)!!
        log.info("visit: {}", visit)
        assertVisit1(visit)
    }

    @Test
    fun insert() {
        val visit = repository.insert(visit(testPet.id))
        assertThat(visit.id).isGreaterThan(0)
        assertVisit(visit, visit(testPet.id))

        val fetched = repository.fetchOneById(visit.id)!!
        assertVisit(fetched, visit(testPet.id), visit.id)
    }

    @Test
    fun update() {
        // Given
        val inserted = repository.insert(visit(testPet.id))
        val visit = inserted.copy(date = LocalDate.of(2023, 3, 15), description = "descr")

        // When
        val updated = repository.update(visit)

        // Then
        assertVisit(updated, visit)
        val fetched = repository.fetchOneById(inserted.id)!!
        assertVisit(fetched, visit)
    }

    companion object {
        fun assertVisit1(visit1: Visit) {
            assertThat(visit1.id).isEqualTo(1)
            assertThat(visit1.petId).isEqualTo(7)
            assertThat(visit1.date).isEqualTo(LocalDate.of(2010, 3, 4))
            assertThat(visit1.description).isEqualTo("rabies shot")
        }
    }
}


