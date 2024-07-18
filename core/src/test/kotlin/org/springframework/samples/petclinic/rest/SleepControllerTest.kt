package org.springframework.samples.petclinic.rest

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.samples.petclinic.test.BaseTest

abstract class SleepControllerTest : BaseTest() {

    @Test
    fun sleep() {
        webClient
            .get()
            .uri("/api/sleep")
            .exchange()
            .expectStatus()
            .isOk
    }

    @Test
    fun `sleep with times and millis`() {
        webClient
            .get()
            .uri("/api/sleep?times={times}&millis={millis}", 2, 5)
            .exchange()
            .expectStatus()
            .isOk
    }

    @Test
    fun `fetch and sleep with times and millis`() {
        webClient
            .get()
            .uri("/api/sleep-and-fetch?times={times}&millis={millis}&strings={strings}&length={length}", 2, 5, 10, 100)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .jsonPath("$.length()").isEqualTo(10)
            .jsonPath("$.[0]")
            .value<String> { string -> Assertions.assertThat(string).hasSize(100) }
    }
}
