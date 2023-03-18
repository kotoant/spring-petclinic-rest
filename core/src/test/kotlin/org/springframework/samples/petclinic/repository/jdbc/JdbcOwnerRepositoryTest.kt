package org.springframework.samples.petclinic.repository.jdbc

import mu.KotlinLogging
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.samples.petclinic.model.Owner
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.model.Visit
import org.springframework.samples.petclinic.repository.jdbc.JdbcPetRepositoryTest.Companion.assertPet7
import org.springframework.samples.petclinic.test.BaseTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate

private val log = KotlinLogging.logger {}

@ActiveProfiles("jdbc")
class JdbcOwnerRepositoryTest : BaseTest() {
    @Autowired
    private lateinit var repository: JdbcOwnerRepository

    @Test
    fun fetchOneById() {
        val owner = repository.fetchOneById(6)!!
        log.info("owner: {}", owner)
        assertOwner6(owner)
    }

    @Test
    fun insert() {
        val owner = repository.insert(owner())
        assertThat(owner.id).isGreaterThan(0)
        assertOwner(owner, owner())

        val fetched = repository.fetchOneById(owner.id)!!
        assertOwner(fetched, owner(), owner.id)
    }

    @Test
    fun update() {
        // Given
        val inserted = repository.insert(owner())
        val owner = inserted.copy(firstName = "First", lastName = "Last", address = "a", city = "c", telephone = "1")

        // When
        val updated = repository.update(owner)

        // Then
        assertOwner(updated, owner)
        val fetched = repository.fetchOneById(inserted.id)!!
        assertOwner(fetched, owner)
    }

    @Test
    fun fetchAll() {
        assertThat(repository.fetchAll()).hasSizeGreaterThanOrEqualTo(10)
    }

    @Test
    fun fetchByLastName() {
        for (owner in repository.fetchByLastName("Abc")) {
            repository.deleteById(owner.id)
        }

        val owner0 = owner().copy(lastName = "Abc")
        val owner1 = owner().copy(lastName = "Abcd")
        repository.insert(owner0)
        repository.insert(owner1)

        val owners2 = repository.fetchByLastName("Abc")
        assertThat(owners2).hasSize(2)
        assertOwner(owners2[0], owner0)
        assertOwner(owners2[1], owner1)

        val owners1 = repository.fetchByLastName("Abcd")
        assertThat(owners1).hasSize(1)
        assertOwner(owners1[0], owner1)

        val owners0 = repository.fetchByLastName("Abcde")
        assertThat(owners0).isEmpty()
    }

    companion object {
        fun assertOwner6(owner6: Owner) {
            assertThat(owner6.id).isEqualTo(6)
            assertThat(owner6.firstName).isEqualTo("Jean")
            assertThat(owner6.lastName).isEqualTo("Coleman")
            assertThat(owner6.address).isEqualTo("105 N. Lake St.")
            assertThat(owner6.city).isEqualTo("Monona")
            assertThat(owner6.telephone).isEqualTo("6085552654")
            assertThat(owner6.pets).hasSize(2)

            assertPet7(owner6.pets[0])
            assertPet8(owner6.pets[1])
        }

        private fun assertPet8(pet8: Pet) {
            assertThat(pet8.id).isEqualTo(8)
            assertThat(pet8.name).isEqualTo("Max")
            assertThat(pet8.birthDate).isEqualTo(LocalDate.of(1995, 9, 4))
            assertThat(pet8.type.id).isEqualTo(1)
            assertThat(pet8.type.name).isEqualTo("cat")
            assertThat(pet8.ownerId).isEqualTo(6)
            assertThat(pet8.visits).hasSize(2)

            assertVisit2(pet8.visits[0])
            assertVisit3(pet8.visits[1])
        }

        private fun assertVisit2(visit2: Visit) {
            assertThat(visit2.id).isEqualTo(2)
            assertThat(visit2.petId).isEqualTo(8)
            assertThat(visit2.date).isEqualTo(LocalDate.of(2011, 3, 4))
            assertThat(visit2.description).isEqualTo("rabies shot")
        }

        private fun assertVisit3(visit3: Visit) {
            assertThat(visit3.id).isEqualTo(3)
            assertThat(visit3.petId).isEqualTo(8)
            assertThat(visit3.date).isEqualTo(LocalDate.of(2009, 6, 4))
            assertThat(visit3.description).isEqualTo("neutered")
        }
    }
}





