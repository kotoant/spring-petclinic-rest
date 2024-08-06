package org.springframework.samples.petclinic.repository.r2dbc.coroutine

import kotlinx.coroutines.delay
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.context.annotation.Profile
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.samples.petclinic.repository.r2dbc.inContextCoroutine
import org.springframework.samples.petclinic.repository.sleep
import org.springframework.samples.petclinic.repository.sleepAndFetch
import org.springframework.samples.petclinic.repository.sleepAndFetchReactive
import org.springframework.samples.petclinic.util.Util
import org.springframework.stereotype.Repository

@Repository
@Profile("r2dbc")
class CoroutineSleepRepository(private val client: DatabaseClient) {
    suspend fun sleep(millis: Int): Unit = client.inContextCoroutine { client.sleep(millis).awaitSingle() }

    suspend fun sleepAndFetch(
        sleep: Boolean, millis: Int, strings: Int, length: Int, jooq: Boolean, db: Boolean
    ): List<String> =
        if (db) {
            if (jooq) client.inContextCoroutine {
                it.sleepAndFetchReactive(sleep, millis, strings, length).awaitSingle()
            }
            else client.sleepAndFetch(sleep, millis, strings, length).awaitSingle()
        } else {
            if (sleep) {
                delay(millis.toLong())
            }
            Util.getRandomStrings(strings, length)
        }
}
