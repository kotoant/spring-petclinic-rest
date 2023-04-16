package org.springframework.samples.petclinic.rest.coroutine

import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.samples.petclinic.rest.api.coroutine.SleepCoroutineApi
import org.springframework.samples.petclinic.service.coroutine.CoroutineClinicService
import org.springframework.web.bind.annotation.RestController

@RestController
@Profile("coroutine")
class CoroutineSleepController(private val clinicService: CoroutineClinicService) : SleepCoroutineApi {
    override suspend fun sleep(times: Int, millis: Int): ResponseEntity<Unit> {
        clinicService.sleep(times, millis)
        return ResponseEntity(HttpStatus.OK)
    }
}
