package org.springframework.samples.petclinic.rest

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.samples.petclinic.rest.dto.PetFieldsDto
import org.springframework.samples.petclinic.rest.dto.PetTypeDto
import org.springframework.samples.petclinic.test.BaseTest
import org.springframework.samples.petclinic.test.RegexMatcher
import java.time.LocalDate

abstract class PetControllerTest : BaseTest() {

    @Test
    fun addPet() {
        val locationMatcher = RegexMatcher(Regex("/api/pets/(\\d+)"))

        webClient
            .post()
            .uri("/api/pets")
            .bodyValue(petMapper.toPetDto(pet(testOwner.id)))
            .exchange()
            .expectStatus()
            .isCreated
            .expectHeader().value("location") { location -> locationMatcher.match(location) }
            .expectBody()
            .jsonPath("$.id")
            .value<Int> { id -> assertThat(id).isPositive.isEqualTo(locationMatcher.destructed.component1().toInt()) }
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
            .json("getPet.json".content(), true)
    }

    @Test
    fun `getPet NotFound`() {
        webClient
            .get()
            .uri("/api/pets/{id}", 100500)
            .exchange()
            .expectStatus()
            .isNotFound
            .expectBody()
            .jsonPath("$.detail").isEqualTo("Pet not found [id: 100500]")
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
            .json("listPets.json".content(), true)
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
            .json("listPets with lastId and pageSize.json".content(), true)
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
                PetFieldsDto(
                    name = "Freddy",
                    birthDate = LocalDate.of(2002, 6, 8),
                    type = PetTypeDto(id = 2, name = "dog")
                )
            )
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .jsonPath("$.id").isEqualTo(pet.id)
            .jsonPath("$.ownerId").isEqualTo(testOwner.id)
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
                PetFieldsDto(
                    name = "Freddy",
                    birthDate = LocalDate.of(2002, 6, 8),
                    type = PetTypeDto(id = 2, name = "dog")
                )
            )
            .exchange()
            .expectStatus()
            .isNotFound
            .expectBody()
            .jsonPath("$.detail").isEqualTo("Pet not found [id: 100500]")
    }
}
