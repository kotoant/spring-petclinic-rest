package org.springframework.samples.petclinic.rest

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.SERVLET
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.samples.petclinic.rest.api.SleepAndFetchApi
import org.springframework.samples.petclinic.rest.api.SleepApi
import org.springframework.samples.petclinic.service.JdbcClinicService
import org.springframework.web.bind.annotation.RestController

@RestController
@ConditionalOnWebApplication(type = SERVLET)
@Profile("jdbc")
class SleepController(private val clinicService: JdbcClinicService) : SleepApi, SleepAndFetchApi {
    override fun sleep(times: Int, millis: Int, zip: Boolean): ResponseEntity<Unit> {
        clinicService.sleep(times, millis)
        return ResponseEntity(HttpStatus.OK)
    }

    override fun sleepAndFetch(
        times: Int, sleep: Boolean, millis: Int, strings: Int, length: Int, jooq: Boolean, db: Boolean
    ): ResponseEntity<List<String>> {
        return ResponseEntity(
            if (db) clinicService.sleepAndFetchWithDb(
                times, sleep, millis, strings, length, jooq
            ) else clinicService.sleepAndFetchWithoutDb(times, sleep, millis, strings, length),
            HttpStatus.OK
        )
    }
}
