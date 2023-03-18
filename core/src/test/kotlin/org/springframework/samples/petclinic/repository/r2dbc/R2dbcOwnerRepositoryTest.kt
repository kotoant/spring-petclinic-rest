package org.springframework.samples.petclinic.repository.r2dbc

import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.samples.petclinic.repository.jdbc.JdbcOwnerRepositoryTest.Companion.assertOwner6
import org.springframework.samples.petclinic.test.BaseTest
import org.springframework.test.context.ActiveProfiles
import reactor.test.StepVerifier

private val log = KotlinLogging.logger {}

@ActiveProfiles("r2dbc")
class R2dbcOwnerRepositoryTest : BaseTest() {
    @Autowired
    private lateinit var repository: R2dbcOwnerRepository

    @Test
    fun fetchOneById() {
        StepVerifier.create(repository.fetchOneById(6))
            .assertNext { owner ->
                log.info("owner: {}", owner)
                assertOwner6(owner)
            }
            .verifyComplete()
    }

    @Test
    fun insert() {
        runBlocking {
            val owner = repository.insert(owner()).awaitSingle()
            assertThat(owner.id).isGreaterThan(0)
            assertOwner(owner, owner())

            val fetched = repository.fetchOneById(owner.id).awaitSingle()
            assertOwner(fetched, owner(), owner.id)
        }
    }

    @Test
    fun update() {
        runBlocking {
            // Given
            val inserted = repository.insert(owner()).awaitSingle()
            val owner =
                inserted.copy(firstName = "First", lastName = "Last", address = "a", city = "c", telephone = "1")

            // When
            val updated = repository.update(owner).awaitSingle()

            // Then
            assertOwner(updated, owner)
            val fetched = repository.fetchOneById(inserted.id).awaitSingle()
            assertOwner(fetched, owner)
        }
    }

    @Test
    fun fetchAll() {
        runBlocking {
            assertThat(repository.fetchAll().awaitSingle()).hasSizeGreaterThanOrEqualTo(10)
        }
    }

    @Test
    fun fetchByLastName() {
        runBlocking {
            for (owner in repository.fetchByLastName("Abc").awaitSingle()) {
                repository.deleteById(owner.id).awaitSingle()
            }

            val owner0 = owner().copy(lastName = "Abc")
            val owner1 = owner().copy(lastName = "Abcd")
            repository.insert(owner0).awaitSingle()
            repository.insert(owner1).awaitSingle()

            val owners2 = repository.fetchByLastName("Abc").awaitSingle()
            assertThat(owners2).hasSize(2)
            assertOwner(owners2[0], owner0)
            assertOwner(owners2[1], owner1)

            val owners1 = repository.fetchByLastName("Abcd").awaitSingle()
            assertThat(owners1).hasSize(1)
            assertOwner(owners1[0], owner1)

            val owners0 = repository.fetchByLastName("Abcde").awaitSingle()
            assertThat(owners0).isEmpty()
        }
    }
}
