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
            .jsonPath("$.id").isEqualTo(6)
            .jsonPath("$.firstName").isEqualTo("Jean")
            .jsonPath("$.lastName").isEqualTo("Coleman")
            .jsonPath("$.address").isEqualTo("105 N. Lake St.")
            .jsonPath("$.city").isEqualTo("Monona")
            .jsonPath("$.telephone").isEqualTo("6085552654")
            .jsonPath("$.pets.length()").isEqualTo(2)

            .jsonPath("$.pets[0].id").isEqualTo(7)
            .jsonPath("$.pets[0].name").isEqualTo("Samantha")
            .jsonPath("$.pets[0].birthDate").isEqualTo("1995-09-04")
            .jsonPath("$.pets[0].type.id").isEqualTo(1)
            .jsonPath("$.pets[0].type.name").isEqualTo("cat")
            .jsonPath("$.pets[0].ownerId").isEqualTo(6)
            .jsonPath("$.pets[0].visits.length()").isEqualTo(2)

            .jsonPath("$.pets[0].visits[0].id").isEqualTo(1)
            .jsonPath("$.pets[0].visits[0].petId").isEqualTo(7)
            .jsonPath("$.pets[0].visits[0].date").isEqualTo("2010-03-04")
            .jsonPath("$.pets[0].visits[0].description").isEqualTo("rabies shot")

            .jsonPath("$.pets[0].visits[1].id").isEqualTo(4)
            .jsonPath("$.pets[0].visits[1].petId").isEqualTo(7)
            .jsonPath("$.pets[0].visits[1].date").isEqualTo("2008-09-04")
            .jsonPath("$.pets[0].visits[1].description").isEqualTo("spayed")

            .jsonPath("$.pets[1].id").isEqualTo(8)
            .jsonPath("$.pets[1].name").isEqualTo("Max")
            .jsonPath("$.pets[1].birthDate").isEqualTo("1995-09-04")
            .jsonPath("$.pets[1].type.id").isEqualTo(1)
            .jsonPath("$.pets[1].type.name").isEqualTo("cat")
            .jsonPath("$.pets[1].ownerId").isEqualTo(6)
            .jsonPath("$.pets[1].visits.length()").isEqualTo(2)

            .jsonPath("$.pets[1].visits[0].id").isEqualTo(2)
            .jsonPath("$.pets[1].visits[0].petId").isEqualTo(8)
            .jsonPath("$.pets[1].visits[0].date").isEqualTo("2011-03-04")
            .jsonPath("$.pets[1].visits[0].description").isEqualTo("rabies shot")

            .jsonPath("$.pets[1].visits[1].id").isEqualTo(3)
            .jsonPath("$.pets[1].visits[1].petId").isEqualTo(8)
            .jsonPath("$.pets[1].visits[1].date").isEqualTo("2009-06-04")
            .jsonPath("$.pets[1].visits[1].description").isEqualTo("neutered")
    }

    @Test
    fun `getOwner NotFound`() {
        webClient
            .get()
            .uri("/api/owners/{id}", 100500)
            .exchange()
            .expectStatus()
            .isNotFound
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
            .jsonPath("$.length()")
            .value<Int> { numberOfOwners -> assertThat(numberOfOwners).isGreaterThanOrEqualTo(10) }

            .jsonPath("$.[0].id").isEqualTo(1)
            .jsonPath("$.[0].firstName").isEqualTo("George")
            .jsonPath("$.[0].lastName").isEqualTo("Franklin")
            .jsonPath("$.[0].address").isEqualTo("110 W. Liberty St.")
            .jsonPath("$.[0].city").isEqualTo("Madison")
            .jsonPath("$.[0].telephone").isEqualTo("6085551023")
            .jsonPath("$.[0].pets.length()").isEqualTo(1)

            .jsonPath("$.[0].pets[0].id").isEqualTo(1)
            .jsonPath("$.[0].pets[0].name").isEqualTo("Leo")
            .jsonPath("$.[0].pets[0].birthDate").isEqualTo("2000-09-07")
            .jsonPath("$.[0].pets[0].type.id").isEqualTo(1)
            .jsonPath("$.[0].pets[0].type.name").isEqualTo("cat")
            .jsonPath("$.[0].pets[0].ownerId").isEqualTo(1)
            .jsonPath("$.[0].pets[0].visits").isEmpty

            .jsonPath("$.[1].id").isEqualTo(2)
            .jsonPath("$.[1].firstName").isEqualTo("Betty")
            .jsonPath("$.[1].lastName").isEqualTo("Davis")
            .jsonPath("$.[1].address").isEqualTo("638 Cardinal Ave.")
            .jsonPath("$.[1].city").isEqualTo("Sun Prairie")
            .jsonPath("$.[1].telephone").isEqualTo("6085551749")
            .jsonPath("$.[1].pets.length()").isEqualTo(1)

            .jsonPath("$.[1].pets[0].id").isEqualTo(2)
            .jsonPath("$.[1].pets[0].name").isEqualTo("Basil")
            .jsonPath("$.[1].pets[0].birthDate").isEqualTo("2002-08-06")
            .jsonPath("$.[1].pets[0].type.id").isEqualTo(6)
            .jsonPath("$.[1].pets[0].type.name").isEqualTo("hamster")
            .jsonPath("$.[1].pets[0].ownerId").isEqualTo(2)
            .jsonPath("$.[1].pets[0].visits").isEmpty
    }

    @Test
    fun `listOwners with lastId and pageSize`() {
        webClient
            .get()
            .uri("/api/owners?lastId={lastId}&pageSize={pageSize}", 5, 1)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .jsonPath("$.length()").isEqualTo(1)

            .jsonPath("$.[0].id").isEqualTo(6)
            .jsonPath("$.[0].firstName").isEqualTo("Jean")
            .jsonPath("$.[0].lastName").isEqualTo("Coleman")
            .jsonPath("$.[0].address").isEqualTo("105 N. Lake St.")
            .jsonPath("$.[0].city").isEqualTo("Monona")
            .jsonPath("$.[0].telephone").isEqualTo("6085552654")
            .jsonPath("$.[0].pets.length()").isEqualTo(2)

            .jsonPath("$.[0].pets[0].id").isEqualTo(7)
            .jsonPath("$.[0].pets[0].name").isEqualTo("Samantha")
            .jsonPath("$.[0].pets[0].birthDate").isEqualTo("1995-09-04")
            .jsonPath("$.[0].pets[0].type.id").isEqualTo(1)
            .jsonPath("$.[0].pets[0].type.name").isEqualTo("cat")
            .jsonPath("$.[0].pets[0].ownerId").isEqualTo(6)
            .jsonPath("$.[0].pets[0].visits.length()").isEqualTo(2)

            .jsonPath("$.[0].pets[0].visits[0].id").isEqualTo(1)
            .jsonPath("$.[0].pets[0].visits[0].petId").isEqualTo(7)
            .jsonPath("$.[0].pets[0].visits[0].date").isEqualTo("2010-03-04")
            .jsonPath("$.[0].pets[0].visits[0].description").isEqualTo("rabies shot")

            .jsonPath("$.[0].pets[0].visits[1].id").isEqualTo(4)
            .jsonPath("$.[0].pets[0].visits[1].petId").isEqualTo(7)
            .jsonPath("$.[0].pets[0].visits[1].date").isEqualTo("2008-09-04")
            .jsonPath("$.[0].pets[0].visits[1].description").isEqualTo("spayed")

            .jsonPath("$.[0].pets[1].id").isEqualTo(8)
            .jsonPath("$.[0].pets[1].name").isEqualTo("Max")
            .jsonPath("$.[0].pets[1].birthDate").isEqualTo("1995-09-04")
            .jsonPath("$.[0].pets[1].type.id").isEqualTo(1)
            .jsonPath("$.[0].pets[1].type.name").isEqualTo("cat")
            .jsonPath("$.[0].pets[1].ownerId").isEqualTo(6)
            .jsonPath("$.[0].pets[1].visits.length()").isEqualTo(2)

            .jsonPath("$.[0].pets[1].visits[0].id").isEqualTo(2)
            .jsonPath("$.[0].pets[1].visits[0].petId").isEqualTo(8)
            .jsonPath("$.[0].pets[1].visits[0].date").isEqualTo("2011-03-04")
            .jsonPath("$.[0].pets[1].visits[0].description").isEqualTo("rabies shot")

            .jsonPath("$.[0].pets[1].visits[1].id").isEqualTo(3)
            .jsonPath("$.[0].pets[1].visits[1].petId").isEqualTo(8)
            .jsonPath("$.[0].pets[1].visits[1].date").isEqualTo("2009-06-04")
            .jsonPath("$.[0].pets[1].visits[1].description").isEqualTo("neutered")
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
    }
}
