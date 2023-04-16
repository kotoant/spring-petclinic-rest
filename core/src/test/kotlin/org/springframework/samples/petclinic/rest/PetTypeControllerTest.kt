package org.springframework.samples.petclinic.rest

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.samples.petclinic.model.PetType
import org.springframework.samples.petclinic.rest.dto.PetTypeFieldsDto
import org.springframework.samples.petclinic.test.BaseTest
import org.springframework.samples.petclinic.test.RegexMatcher

abstract class PetTypeControllerTest : BaseTest() {


    @Test
    fun addPetType() {
        val locationMatcher = RegexMatcher(Regex("/api/petTypes/(\\d+)"))

        webClient
            .post()
            .uri("/api/pettypes")
            .bodyValue(PetTypeFieldsDto(name = "rabbit"))
            .exchange()
            .expectStatus()
            .isCreated
            .expectHeader().value("location") { location -> locationMatcher.match(location) }
            .expectBody()
            .jsonPath("$.id")
            .value<Int> { id -> assertThat(id).isPositive.isEqualTo(locationMatcher.destructed.component1().toInt()) }
            .jsonPath("$.name").isEqualTo("rabbit")
    }

    @Test
    fun `deletePetType NoContent`() {
        val petType = petTypeRepository.insert(PetType(0, "rat"))

        webClient
            .delete()
            .uri("/api/pettypes/{id}", petType.id)
            .exchange()
            .expectStatus()
            .isNoContent

        assertThat(petTypeRepository.fetchOneById(petType.id)).isNull()

        webClient
            .delete()
            .uri("/api/pettypes/{id}", petType.id)
            .exchange()
            .expectStatus()
            .isNotFound
    }

    @Test
    fun `deletePetType NotFound`() {
        webClient
            .delete()
            .uri("/api/pettypes/{id}", 100500)
            .exchange()
            .expectStatus()
            .isNotFound
    }

    @Test
    fun `getPetType Ok`() {
        webClient
            .get()
            .uri("/api/pettypes/{id}", 1)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .json("getPetType.json".content(), true)
    }

    @Test
    fun `getPetType NotFound`() {
        webClient
            .get()
            .uri("/api/pettypes/{id}", 100500)
            .exchange()
            .expectStatus()
            .isNotFound
            .expectBody()
            .jsonPath("$.detail").isEqualTo("Pet type not found [id: 100500]")
    }

    @Test
    fun listPetTypes() {
        webClient
            .get()
            .uri("/api/pettypes")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .jsonPath("$.length()")
            .value<Int> { numberOfPetTypes -> assertThat(numberOfPetTypes).isGreaterThanOrEqualTo(6) }

            .jsonPath("$[0].id").isEqualTo(1)
            .jsonPath("$[0].name").isEqualTo("cat")

            .jsonPath("$[1].id").isEqualTo(2)
            .jsonPath("$[1].name").isEqualTo("dog")

            .jsonPath("$[2].id").isEqualTo(3)
            .jsonPath("$[2].name").isEqualTo("lizard")

            .jsonPath("$[3].id").isEqualTo(4)
            .jsonPath("$[3].name").isEqualTo("snake")

            .jsonPath("$[4].id").isEqualTo(5)
            .jsonPath("$[4].name").isEqualTo("bird")

            .jsonPath("$[5].id").isEqualTo(6)
            .jsonPath("$[5].name").isEqualTo("hamster")
    }

    @Test
    fun `listPetTypes with lastId and pageSize`() {
        webClient
            .get()
            .uri("/api/pettypes?lastId={lastId}&pageSize={pageSize}", 1, 2)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .json("listPetTypes with lastId and pageSize.json".content(), true)
    }

    @Test
    fun `listPetTypes NotFound`() {
        webClient
            .get()
            .uri("/api/pettypes?lastId={lastId}", 100500)
            .exchange()
            .expectStatus()
            .isNotFound
    }

    @Test
    fun `updatePetType Ok`() {
        val petType = petTypeRepository.insert(PetType(0, "rat"))

        webClient
            .put()
            .uri("/api/pettypes/{id}", petType.id)
            .bodyValue(PetTypeFieldsDto(name = "chinchilla"))
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .jsonPath("$.id").isEqualTo(petType.id)
            .jsonPath("$.name").isEqualTo("chinchilla")
    }

    @Test
    fun `updatePetType NotFound`() {
        webClient
            .put()
            .uri("/api/pettypes/{id}", 100500)
            .bodyValue(
                PetTypeFieldsDto(
                    name = "guinea pig"
                )
            )
            .exchange()
            .expectStatus()
            .isNotFound
            .expectBody()
            .jsonPath("$.detail").isEqualTo("Pet type not found [id: 100500]")
    }
}
