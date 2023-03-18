package org.springframework.samples.petclinic.repository.r2dbc

import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.samples.petclinic.model.PetType
import org.springframework.samples.petclinic.repository.jdbc.JdbcPetRepositoryTest.Companion.assertPet7
import org.springframework.samples.petclinic.test.BaseTest
import org.springframework.test.context.ActiveProfiles
import reactor.test.StepVerifier
import java.time.LocalDate

private val log = KotlinLogging.logger {}

@ActiveProfiles("r2dbc")
class R2dbcPetRepositoryTest : BaseTest() {
    @Autowired
    private lateinit var repository: R2dbcPetRepository

    @Test
    fun fetchOneById() {
        StepVerifier.create(repository.fetchOneById(7))
            .assertNext { pet ->
                log.info("pet: {}", pet)
                assertPet7(pet)
            }
            .verifyComplete()
    }

    @Test
    fun insert() {
        runBlocking {
            val pet = repository.insert(pet(testOwner.id)).awaitSingle()
            Assertions.assertThat(pet.id).isGreaterThan(0)
            assertPet(pet, pet(testOwner.id))

            val fetched = repository.fetchOneById(pet.id).awaitSingle()
            assertPet(fetched, pet(testOwner.id), pet.id)
        }
    }

    @Test
    fun update() {
        runBlocking {
            // Given
            val inserted = repository.insert(pet(testOwner.id)).awaitSingle()
            val pet = inserted.copy(
                name = "New", birthDate = LocalDate.of(2023, 3, 10), type = PetType(
                    2,
                    "dog"
                )
            )

            // When
            val updated = repository.update(pet).awaitSingle()

            // Then
            assertPet(updated, pet)
            val fetched = repository.fetchOneById(inserted.id).awaitSingle()
            assertPet(fetched, pet)
        }
    }
}
