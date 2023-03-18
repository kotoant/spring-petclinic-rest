package org.springframework.samples.petclinic.rest

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.samples.petclinic.model.Visit
import org.springframework.samples.petclinic.rest.dto.VisitDto
import org.springframework.samples.petclinic.test.BaseTest
import java.time.LocalDate

abstract class VisitControllerTest : BaseTest() {

    @Test
    fun addVisit() {
        webClient
            .post()
            .uri("/api/visits")
            .bodyValue(VisitDto(LocalDate.of(2023, 3, 14), "description", null, testPet.id))
            .exchange()
            .expectStatus()
            .isCreated
            .expectBody()
            .jsonPath("$.id").value<Int> { id -> assertThat(id).isPositive() }
            .jsonPath("$.petId").isEqualTo(testPet.id)
            .jsonPath("$.date").isEqualTo("2023-03-14")
            .jsonPath("$.description").isEqualTo("description")
    }

    @Test
    fun `deleteVisit NoContent`() {
        val visit = visitRepository.insert(Visit(0, testPet.id, LocalDate.of(2023, 3, 16), "description"))

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
            .jsonPath("$.id").isEqualTo(1)
            .jsonPath("$.petId").isEqualTo(7)
            .jsonPath("$.date").isEqualTo("2010-03-04")
            .jsonPath("$.description").isEqualTo("rabies shot")
    }

    @Test
    fun `getVisit NotFound`() {
        webClient
            .get()
            .uri("/api/visits/{id}", 100500)
            .exchange()
            .expectStatus()
            .isNotFound
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
            .jsonPath("$.length()").isEqualTo(2)

            .jsonPath("$.[0].id").isEqualTo(2)
            .jsonPath("$.[0].petId").isEqualTo(8)
            .jsonPath("$.[0].date").isEqualTo("2011-03-04")
            .jsonPath("$.[0].description").isEqualTo("rabies shot")

            .jsonPath("$.[1].id").isEqualTo(3)
            .jsonPath("$.[1].petId").isEqualTo(8)
            .jsonPath("$.[1].date").isEqualTo("2009-06-04")
            .jsonPath("$.[1].description").isEqualTo("neutered")
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
        val visit = visitRepository.insert(Visit(0, testPet.id, LocalDate.of(2023, 3, 16), "description"))

        webClient
            .put()
            .uri("/api/visits/{id}", visit.id)
            .bodyValue(visitMapper.toVisitDto(visit.copy(date = LocalDate.of(2023, 3, 15), description = "descr")))
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
                visitMapper.toVisitDto(
                    testVisit.copy(
                        id = 100500,
                        date = LocalDate.of(2023, 3, 15),
                        description = "descr"
                    )
                )
            )
            .exchange()
            .expectStatus()
            .isNotFound
    }
}
