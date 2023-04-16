package org.springframework.samples.petclinic.rest

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.samples.petclinic.rest.dto.OwnerFieldsDto
import org.springframework.samples.petclinic.test.BaseTest
import org.springframework.samples.petclinic.test.RegexMatcher

abstract class OwnerControllerTest : BaseTest() {

    @Test
    fun addOwner() {
        val locationMatcher = RegexMatcher(Regex("/api/owners/(\\d+)"))

        webClient
            .post()
            .uri("/api/owners")
            .bodyValue(ownerMapper.toOwnerFieldsDto(owner()))
            .exchange()
            .expectStatus()
            .isCreated
            .expectHeader().value("location") { location -> locationMatcher.match(location) }
            .expectBody()
            .jsonPath("$.id")
            .value<Int> { id -> assertThat(id).isPositive.isEqualTo(locationMatcher.destructed.component1().toInt()) }
            .jsonPath("$.firstName").isEqualTo("Firstname")
            .jsonPath("$.lastName").isEqualTo("Lastname")
            .jsonPath("$.address").isEqualTo("address")
            .jsonPath("$.city").isEqualTo("city")
            .jsonPath("$.telephone").isEqualTo("1234567")
            .jsonPath("$.pets").isEmpty
    }

    @Test
    fun addPetToOwner() {
        val owner = ownerRepository.insert(owner())

        val locationMatcher = RegexMatcher(Regex("/api/pets/(\\d+)"))

        webClient
            .post()
            .uri("/api/owners/{id}/pets", owner.id)
            .bodyValue(petMapper.toPetFieldsDto(pet(owner.id)))
            .exchange()
            .expectStatus()
            .isCreated
            .expectHeader().value("location") { location -> locationMatcher.match(location) }
            .expectBody()
            .jsonPath("$.id")
            .value<Int> { id -> assertThat(id).isPositive.isEqualTo(locationMatcher.destructed.component1().toInt()) }
            .jsonPath("$.ownerId").isEqualTo(owner.id)
            .jsonPath("$.birthDate").isEqualTo("2023-03-14")
            .jsonPath("$.type.id").isEqualTo(1)
            .jsonPath("$.type.name").isEqualTo("cat")
    }

    @Test
    fun addVisitToOwner() {
        val owner = ownerRepository.insert(owner())
        val pet = petRepository.insert(pet(owner.id))

        val locationMatcher = RegexMatcher(Regex("/api/visits/(\\d+)"))

        webClient
            .post()
            .uri("/api/owners/{ownerId}/pets/{petId}/visits", owner.id, pet.id)
            .bodyValue(visitMapper.toVisitDto(visit(pet.id)))
            .exchange()
            .expectStatus()
            .isCreated
            .expectHeader().value("location") { location -> locationMatcher.match(location) }
            .expectBody()
            .jsonPath("$.id")
            .value<Int> { id -> assertThat(id).isPositive.isEqualTo(locationMatcher.destructed.component1().toInt()) }
            .jsonPath("$.petId").isEqualTo(pet.id)
            .jsonPath("$.date").isEqualTo("2023-03-14")
            .jsonPath("$.description").isEqualTo("description")
    }

    @Test
    fun `deleteOwner NoContent`() {
        val owner = ownerRepository.insert(owner())

        webClient
            .delete()
            .uri("/api/owners/{id}", owner.id)
            .exchange()
            .expectStatus()
            .isNoContent

        assertThat(ownerRepository.fetchOneById(owner.id)).isNull()

        webClient
            .delete()
            .uri("/api/owners/{id}", owner.id)
            .exchange()
            .expectStatus()
            .isNotFound
    }

    @Test
    fun `deleteOwner NotFound`() {
        webClient
            .delete()
            .uri("/api/owners/{id}", 100500)
            .exchange()
            .expectStatus()
            .isNotFound
    }

    @Test
    fun `getOwner Ok`() {
        webClient
            .get()
            .uri("/api/owners/{id}", 6)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .json("getOwner.json".content(), true)
    }

    @Test
    fun `getOwner NotFound`() {
        webClient
            .get()
            .uri("/api/owners/{id}", 100500)
            .exchange()
            .expectStatus()
            .isNotFound
            .expectBody()
            .jsonPath("$.detail").isEqualTo("Owner not found [id: 100500]")
    }

    @Test
    fun listOwners() {
        webClient
            .get()
            .uri("/api/owners")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .json("listOwners.json".content(), true)
    }

    @Test
    fun `listOwners with lastId and pageSize`() {
        webClient
            .get()
            .uri("/api/owners?lastId={lastId}&pageSize={pageSize}", 5, 1)
            .exchange()
            .expectAll()
            .expectStatus()
            .isOk
            .expectBody()
            .json("listOwners with lastId and pageSize.json".content(), true)
    }

    @Test
    fun `listOwners with lastName`() {
        webClient
            .get()
            .uri("/api/owners?lastName={lastName}", "Es")
            .exchange()
            .expectAll()
            .expectStatus()
            .isOk
            .expectBody()
            .json("listOwners with lastName.json".content(), true)
    }

    @Test
    fun `listOwners NotFound`() {
        webClient
            .get()
            .uri("/api/owners?lastId={lastId}", 100500)
            .exchange()
            .expectStatus()
            .isNotFound
    }

    @Test
    fun `updateOwner Ok`() {
        val owner = ownerRepository.insert(testOwner)

        webClient
            .put()
            .uri("/api/owners/{id}", owner.id)
            .bodyValue(
                OwnerFieldsDto(
                    firstName = "First",
                    lastName = "Last",
                    address = "a",
                    city = "c",
                    telephone = "1"
                )
            )
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .jsonPath("$.id").isEqualTo(owner.id)
            .jsonPath("$.firstName").isEqualTo("First")
            .jsonPath("$.lastName").isEqualTo("Last")
            .jsonPath("$.address").isEqualTo("a")
            .jsonPath("$.city").isEqualTo("c")
            .jsonPath("$.telephone").isEqualTo("1")
    }

    @Test
    fun `updateOwner NotFound`() {
        webClient
            .put()
            .uri("/api/owners/{id}", 100500)
            .bodyValue(
                OwnerFieldsDto(
                    firstName = "First",
                    lastName = "Last",
                    address = "a",
                    city = "c",
                    telephone = "1"
                )
            )
            .exchange()
            .expectStatus()
            .isNotFound
            .expectBody()
            .jsonPath("$.detail").isEqualTo("Owner not found [id: 100500]")
    }
}
