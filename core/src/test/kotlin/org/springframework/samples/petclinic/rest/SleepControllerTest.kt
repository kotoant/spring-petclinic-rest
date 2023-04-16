package org.springframework.samples.petclinic.rest

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
}
