package org.springframework.samples.petclinic.rest.coroutine

import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.samples.petclinic.rest.api.coroutine.SleepAndFetchCoroutineApi
import org.springframework.samples.petclinic.rest.api.coroutine.SleepCoroutineApi
import org.springframework.samples.petclinic.service.coroutine.CoroutineClinicService
import org.springframework.web.bind.annotation.RestController

@RestController
@Profile("coroutine")
class CoroutineSleepController(private val clinicService: CoroutineClinicService) : SleepCoroutineApi,
    SleepAndFetchCoroutineApi {
    override suspend fun sleep(times: Int, millis: Int, zip: Boolean): ResponseEntity<Unit> {
        clinicService.sleep(times, millis, zip)
        return ResponseEntity(HttpStatus.OK)
    }

    override suspend fun sleepAndFetch(
        times: Int, millis: Int, strings: Int, length: Int
    ): ResponseEntity<List<String>> {
        val result = clinicService.sleepAndFetch(times, millis, strings, length)
        return ResponseEntity(result, HttpStatus.OK)
    }
}
