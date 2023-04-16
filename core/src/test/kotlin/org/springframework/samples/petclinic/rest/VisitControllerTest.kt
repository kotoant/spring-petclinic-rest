package org.springframework.samples.petclinic.rest

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.samples.petclinic.rest.dto.VisitFieldsDto
import org.springframework.samples.petclinic.test.BaseTest
import org.springframework.samples.petclinic.test.RegexMatcher
import java.time.LocalDate

abstract class VisitControllerTest : BaseTest() {

    @Test
    fun addVisit() {
        val locationMatcher = RegexMatcher(Regex("/api/visits/(\\d+)"))

        webClient
            .post()
            .uri("/api/visits")
            .bodyValue(visitMapper.toVisitDto(visit(testPet.id)))
            .exchange()
            .expectStatus()
            .isCreated
            .expectHeader().value("location") { location -> locationMatcher.match(location) }
            .expectBody()
            .jsonPath("$.id")
            .value<Int> { id -> assertThat(id).isPositive.isEqualTo(locationMatcher.destructed.component1().toInt()) }
            .jsonPath("$.petId").isEqualTo(testPet.id)
            .jsonPath("$.date").isEqualTo("2023-03-14")
            .jsonPath("$.description").isEqualTo("description")
    }

    @Test
    fun `deleteVisit NoContent`() {
        val visit = visitRepository.insert(visit(testPet.id))

        webClient
            .delete()
            .uri("/api/visits/{id}", visit.id)
            .exchange()
            .expectStatus()
            .isNoContent

        assertThat(visitRepository.fetchOneById(visit.id)).isNull()

        webClient
            .delete()
            .uri("/api/visits/{id}", visit.id)
            .exchange()
            .expectStatus()
            .isNotFound
    }

    @Test
    fun `deleteVisit NotFound`() {
        webClient
            .delete()
            .uri("/api/visits/{id}", 100500)
            .exchange()
            .expectStatus()
            .isNotFound
    }

    @Test
    fun `getVisit Ok`() {
        webClient
            .get()
            .uri("/api/visits/{id}", 1)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .json("getVisit.json".content(), true)
    }

    @Test
    fun `getVisit NotFound`() {
        webClient
            .get()
            .uri("/api/visits/{id}", 100500)
            .exchange()
            .expectStatus()
            .isNotFound
            .expectBody()
            .jsonPath("$.detail").isEqualTo("Visit not found [id: 100500]")
    }

    @Test
    fun listVisits() {
        webClient
            .get()
            .uri("/api/visits")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .jsonPath("$.length()")
            .value<Int> { numberOfVisits -> assertThat(numberOfVisits).isGreaterThanOrEqualTo(4) }

            .jsonPath("$.[0].id").isEqualTo(1)
            .jsonPath("$.[0].petId").isEqualTo(7)
            .jsonPath("$.[0].date").isEqualTo("2010-03-04")
            .jsonPath("$.[0].description").isEqualTo("rabies shot")

            .jsonPath("$.[1].id").isEqualTo(2)
            .jsonPath("$.[1].petId").isEqualTo(8)
            .jsonPath("$.[1].date").isEqualTo("2011-03-04")
            .jsonPath("$.[1].description").isEqualTo("rabies shot")

            .jsonPath("$.[2].id").isEqualTo(3)
            .jsonPath("$.[2].petId").isEqualTo(8)
            .jsonPath("$.[2].date").isEqualTo("2009-06-04")
            .jsonPath("$.[2].description").isEqualTo("neutered")

            .jsonPath("$.[3].id").isEqualTo(4)
            .jsonPath("$.[3].petId").isEqualTo(7)
            .jsonPath("$.[3].date").isEqualTo("2008-09-04")
            .jsonPath("$.[3].description").isEqualTo("spayed")
    }

    @Test
    fun `listVisits with lastId and pageSize`() {
        webClient
            .get()
            .uri("/api/visits?lastId={lastId}&pageSize={pageSize}", 1, 2)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .json("listVisits with lastId and pageSize.json".content(), true)
    }

    @Test
    fun `listVisits NotFound`() {
        webClient
            .get()
            .uri("/api/visits?lastId={lastId}", 100500)
            .exchange()
            .expectStatus()
            .isNotFound
    }

    @Test
    fun `updateVisit Ok`() {
        val visit = visitRepository.insert(visit(testPet.id))

        webClient
            .put()
            .uri("/api/visits/{id}", visit.id)
            .bodyValue(
                VisitFieldsDto(
                    date = LocalDate.of(2023, 3, 15),
                    description = "descr"
                )
            )
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .jsonPath("$.id").isEqualTo(visit.id)
            .jsonPath("$.petId").isEqualTo(visit.petId)
            .jsonPath("$.date").isEqualTo("2023-03-15")
            .jsonPath("$.description").isEqualTo("descr")
    }

    @Test
    fun `updateVisit NotFound`() {
        webClient
            .put()
            .uri("/api/visits/{id}", 100500)
            .bodyValue(
                VisitFieldsDto(
                    date = LocalDate.of(2023, 3, 15),
                    description = "descr"
                )
            )
            .exchange()
            .expectStatus()
            .isNotFound
            .expectBody()
            .jsonPath("$.detail").isEqualTo("Visit not found [id: 100500]")
    }
}
