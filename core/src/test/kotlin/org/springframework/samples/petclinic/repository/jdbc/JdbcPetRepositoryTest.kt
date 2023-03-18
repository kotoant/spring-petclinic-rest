package org.springframework.samples.petclinic.repository.jdbc

import mu.KotlinLogging
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.model.PetType
import org.springframework.samples.petclinic.model.Visit
import org.springframework.samples.petclinic.repository.jdbc.JdbcVisitRepositoryTest.Companion.assertVisit1
import org.springframework.samples.petclinic.test.BaseTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate

private val log = KotlinLogging.logger {}

@ActiveProfiles("jdbc")
class JdbcPetRepositoryTest : BaseTest() {
    @Autowired
    private lateinit var repository: JdbcPetRepository

    @Test
    fun fetchOneById() {
        val pet = repository.fetchOneById(7)!!
        log.info("pet: {}", pet)
        assertPet7(pet)
    }

    @Test
    fun insert() {
        val pet = repository.insert(pet(testOwner.id))
        assertThat(pet.id).isGreaterThan(0)
        assertPet(pet, pet(testOwner.id))

        val fetched = repository.fetchOneById(pet.id)!!
        assertPet(fetched, pet(testOwner.id), pet.id)
    }

    @Test
    fun update() {
        // Given
        val inserted = repository.insert(pet(testOwner.id))
        val pet = inserted.copy(
            name = "New", birthDate = LocalDate.of(2023, 3, 10), type = PetType(
                2,
                "dog"
            )
        )

        // When
        val updated = repository.update(pet)

        // Then
        assertPet(updated, pet)
        val fetched = repository.fetchOneById(inserted.id)!!
        assertPet(fetched, pet)
    }

    companion object {
        fun assertPet7(pet7: Pet) {
            assertThat(pet7.id).isEqualTo(7)
            assertThat(pet7.name).isEqualTo("Samantha")
            assertThat(pet7.birthDate).isEqualTo(LocalDate.of(1995, 9, 4))
            assertThat(pet7.type.id).isEqualTo(1)
            assertThat(pet7.type.name).isEqualTo("cat")
            assertThat(pet7.ownerId).isEqualTo(6)
            assertThat(pet7.visits).hasSize(2)

            assertVisit1(pet7.visits[0])
            assertVisit4(pet7.visits[1])
        }

        private fun assertVisit4(visit4: Visit) {
            assertThat(visit4.id).isEqualTo(4)
            assertThat(visit4.petId).isEqualTo(7)
            assertThat(visit4.date).isEqualTo(LocalDate.of(2008, 9, 4))
            assertThat(visit4.description).isEqualTo("spayed")
        }
    }
}

