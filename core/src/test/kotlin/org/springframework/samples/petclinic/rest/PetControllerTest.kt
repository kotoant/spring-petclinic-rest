package org.springframework.samples.petclinic.rest

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.samples.petclinic.model.PetType
import org.springframework.samples.petclinic.test.BaseTest
import java.time.LocalDate

abstract class PetControllerTest : BaseTest() {

    @Test
    fun addPet() {
        webClient
            .post()
            .uri("/api/pets")
            .bodyValue(petMapper.toPetDto(pet(testOwner.id)))
            .exchange()
            .expectStatus()
            .isCreated
            .expectBody()
            .jsonPath("$.id").value<Int> { id -> assertThat(id).isPositive() }
            .jsonPath("$.ownerId").isEqualTo(testOwner.id)
            .jsonPath("$.birthDate").isEqualTo("2023-03-14")
            .jsonPath("$.type.id").isEqualTo(1)
            .jsonPath("$.type.name").isEqualTo("cat")
    }

    @Test
    fun `deletePet NoContent`() {
        val pet = petRepository.insert(pet(testOwner.id))

        webClient
            .delete()
            .uri("/api/pets/{id}", pet.id)
            .exchange()
            .expectStatus()
            .isNoContent

        assertThat(petRepository.fetchOneById(pet.id)).isNull()

        webClient
            .delete()
            .uri("/api/pets/{id}", pet.id)
            .exchange()
            .expectStatus()
            .isNotFound
    }

    @Test
    fun `deletePet NotFound`() {
        webClient
            .delete()
            .uri("/api/pets/{id}", 100500)
            .exchange()
            .expectStatus()
            .isNotFound
    }

    @Test
    fun `getPet Ok`() {
        webClient
            .get()
            .uri("/api/pets/{id}", 7)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .jsonPath("$.id").isEqualTo(7)
            .jsonPath("$.name").isEqualTo("Samantha")
            .jsonPath("$.birthDate").isEqualTo("1995-09-04")
            .jsonPath("$.type.id").isEqualTo(1)
            .jsonPath("$.type.name").isEqualTo("cat")
            .jsonPath("$.ownerId").isEqualTo(6)
            .jsonPath("$.visits.length()").isEqualTo(2)

            .jsonPath("$.visits[0].id").isEqualTo(1)
            .jsonPath("$.visits[0].petId").isEqualTo(7)
            .jsonPath("$.visits[0].date").isEqualTo("2010-03-04")
            .jsonPath("$.visits[0].description").isEqualTo("rabies shot")

            .jsonPath("$.visits[1].id").isEqualTo(4)
            .jsonPath("$.visits[1].petId").isEqualTo(7)
            .jsonPath("$.visits[1].date").isEqualTo("2008-09-04")
            .jsonPath("$.visits[1].description").isEqualTo("spayed")
    }

    @Test
    fun `getPet NotFound`() {
        webClient
            .get()
            .uri("/api/pets/{id}", 100500)
            .exchange()
            .expectStatus()
            .isNotFound
    }

    @Test
    fun listPets() {
        webClient
            .get()
            .uri("/api/pets")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .jsonPath("$.length()")
            .value<Int> { numberOfPets -> assertThat(numberOfPets).isGreaterThanOrEqualTo(4) }

            .jsonPath("$[0].id").isEqualTo(1)
            .jsonPath("$[0].name").isEqualTo("Leo")
            .jsonPath("$[0].birthDate").isEqualTo("2000-09-07")
            .jsonPath("$[0].type.id").isEqualTo(1)
            .jsonPath("$[0].type.name").isEqualTo("cat")
            .jsonPath("$[0].ownerId").isEqualTo(1)
            .jsonPath("$[0].visits").isEmpty

            .jsonPath("$[1].id").isEqualTo(2)
            .jsonPath("$[1].name").isEqualTo("Basil")
            .jsonPath("$[1].birthDate").isEqualTo("2002-08-06")
            .jsonPath("$[1].type.id").isEqualTo(6)
            .jsonPath("$[1].type.name").isEqualTo("hamster")
            .jsonPath("$[1].ownerId").isEqualTo(2)
            .jsonPath("$[1].visits").isEmpty

            .jsonPath("$[2].id").isEqualTo(3)
            .jsonPath("$[2].name").isEqualTo("Rosy")
            .jsonPath("$[2].birthDate").isEqualTo("2001-04-17")
            .jsonPath("$[2].type.id").isEqualTo(2)
            .jsonPath("$[2].type.name").isEqualTo("dog")
            .jsonPath("$[2].ownerId").isEqualTo(3)
            .jsonPath("$[2].visits").isEmpty

            .jsonPath("$[3].id").isEqualTo(4)
            .jsonPath("$[3].name").isEqualTo("Jewel")
            .jsonPath("$[3].birthDate").isEqualTo("2000-03-07")
            .jsonPath("$[3].type.id").isEqualTo(2)
            .jsonPath("$[3].type.name").isEqualTo("dog")
            .jsonPath("$[3].ownerId").isEqualTo(3)
            .jsonPath("$[3].visits").isEmpty
    }

    @Test
    fun `listPets with lastId and pageSize`() {
        webClient
            .get()
            .uri("/api/pets?lastId={lastId}&pageSize={pageSize}", 6, 2)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .jsonPath("$.length()").isEqualTo(2)

            .jsonPath("$[0].id").isEqualTo(7)
            .jsonPath("$[0].name").isEqualTo("Samantha")
            .jsonPath("$[0].birthDate").isEqualTo("1995-09-04")
            .jsonPath("$[0].type.id").isEqualTo(1)
            .jsonPath("$[0].type.name").isEqualTo("cat")
            .jsonPath("$[0].ownerId").isEqualTo(6)
            .jsonPath("$[0].visits.length()").isEqualTo(2)

            .jsonPath("$[0].visits[0].id").isEqualTo(1)
            .jsonPath("$[0].visits[0].petId").isEqualTo(7)
            .jsonPath("$[0].visits[0].date").isEqualTo("2010-03-04")
            .jsonPath("$[0].visits[0].description").isEqualTo("rabies shot")

            .jsonPath("$[0].visits[1].id").isEqualTo(4)
            .jsonPath("$[0].visits[1].petId").isEqualTo(7)
            .jsonPath("$[0].visits[1].date").isEqualTo("2008-09-04")
            .jsonPath("$[0].visits[1].description").isEqualTo("spayed")

            .jsonPath("$[1].id").isEqualTo(8)
            .jsonPath("$[1].name").isEqualTo("Max")
            .jsonPath("$[1].birthDate").isEqualTo("1995-09-04")
            .jsonPath("$[1].type.id").isEqualTo(1)
            .jsonPath("$[1].type.name").isEqualTo("cat")
            .jsonPath("$[1].ownerId").isEqualTo(6)
            .jsonPath("$[1].visits.length()").isEqualTo(2)

            .jsonPath("$[1].visits[0].id").isEqualTo(2)
            .jsonPath("$[1].visits[0].petId").isEqualTo(8)
            .jsonPath("$[1].visits[0].date").isEqualTo("2011-03-04")
            .jsonPath("$[1].visits[0].description").isEqualTo("rabies shot")

            .jsonPath("$[1].visits[1].id").isEqualTo(3)
            .jsonPath("$[1].visits[1].petId").isEqualTo(8)
            .jsonPath("$[1].visits[1].date").isEqualTo("2009-06-04")
            .jsonPath("$[1].visits[1].description").isEqualTo("neutered")
    }

    @Test
    fun `listPets NotFound`() {
        webClient
            .get()
            .uri("/api/pets?lastId={lastId}", 100500)
            .exchange()
            .expectStatus()
            .isNotFound
    }

    @Test
    fun `updatePet Ok`() {
        val pet = petRepository.insert(testPet)

        webClient
            .put()
            .uri("/api/pets/{id}", pet.id)
            .bodyValue(
                petMapper.toPetDto(
                    pet.copy(
                        name = "Freddy",
                        birthDate = LocalDate.of(2002, 6, 8),
                        type = PetType(2, "dog")
                    )
                )
            )
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .jsonPath("$.id").isEqualTo(pet.id)
            .jsonPath("$.name").isEqualTo("Freddy")
            .jsonPath("$.birthDate").isEqualTo("2002-06-08")
            .jsonPath("$.type.id").isEqualTo(2)
            .jsonPath("$.type.name").isEqualTo("dog")
    }

    @Test
    fun `updatePet NotFound`() {
        webClient
            .put()
            .uri("/api/pets/{id}", 100500)
            .bodyValue(
                petMapper.toPetDto(
                    testPet.copy(
                        id = 100500,
                        name = "Freddy",
                        birthDate = LocalDate.of(2002, 6, 8),
                        type = PetType(2, "dog")
                    )
                )
            )
            .exchange()
            .expectStatus()
            .isNotFound
    }
}
