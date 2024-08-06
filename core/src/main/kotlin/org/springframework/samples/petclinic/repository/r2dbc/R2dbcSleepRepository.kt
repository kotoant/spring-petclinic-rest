package org.springframework.samples.petclinic.repository.r2dbc

import org.springframework.context.annotation.Profile
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.samples.petclinic.repository.sleep
import org.springframework.samples.petclinic.repository.sleepAndFetch
import org.springframework.samples.petclinic.repository.sleepAndFetchReactive
import org.springframework.samples.petclinic.util.Util
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.time.Duration

@Repository
@Profile("r2dbc")
class R2dbcSleepRepository(private val client: DatabaseClient) {
    fun sleep(millis: Int): Mono<Unit> = client.sleep(millis)

    fun sleepAndFetch(
        sleep: Boolean, millis: Int, strings: Int, length: Int, jooq: Boolean, db: Boolean
    ): Mono<List<String>> =
        if (db) {
            if (jooq) client.inContext { it.sleepAndFetchReactive(sleep, millis, strings, length) }
            else client.sleepAndFetch(sleep, millis, strings, length)
        } else {
            (if (sleep) Mono.delay(Duration.ofMillis(millis.toLong())) else Mono.just(0)).map {
                Util.getRandomStrings(strings, length)
            }
        }
}
