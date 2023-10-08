package org.springframework.samples.petclinic.rest.reactive

import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.samples.petclinic.rest.api.reactive.SleepReactiveApi
import org.springframework.samples.petclinic.service.reactive.ReactiveClinicService
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@Profile("reactive")
class ReactiveSleepController(private val clinicService: ReactiveClinicService) : SleepReactiveApi {
    override fun sleep(times: Int, millis: Int, zip: Boolean): Mono<ResponseEntity<Unit>> {
        return clinicService.sleep(times, millis, zip).map { ResponseEntity(HttpStatus.OK) }
    }
}
