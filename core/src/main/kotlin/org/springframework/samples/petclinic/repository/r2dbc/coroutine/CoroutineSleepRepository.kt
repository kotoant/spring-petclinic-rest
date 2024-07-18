package org.springframework.samples.petclinic.repository.r2dbc.coroutine

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.context.annotation.Profile
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.samples.petclinic.repository.r2dbc.inContextCoroutine
import org.springframework.samples.petclinic.repository.sleep
import org.springframework.samples.petclinic.repository.sleepAndFetch
import org.springframework.stereotype.Repository

@Repository
@Profile("r2dbc")
class CoroutineSleepRepository(private val client: DatabaseClient) {
    suspend fun sleep(millis: Int): Unit = client.inContextCoroutine { client.sleep(millis).awaitSingle() }

    suspend fun sleepAndFetch(millis: Int, strings: Int, length: Int): List<String> =
        client.inContextCoroutine { client.sleepAndFetch(millis, strings, length).awaitSingle() }
}
