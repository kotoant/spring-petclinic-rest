package org.springframework.samples.petclinic.repository.r2dbc

import org.springframework.context.annotation.Profile
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.samples.petclinic.repository.sleep
import org.springframework.samples.petclinic.repository.sleepAndFetch
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
@Profile("r2dbc")
class R2dbcSleepRepository(private val client: DatabaseClient) {
    fun sleep(millis: Int): Mono<Unit> = client.sleep(millis)

    fun sleepAndFetch(millis: Int, strings: Int, length: Int): Mono<List<String>> = client.sleepAndFetch(millis, strings, length)
}
