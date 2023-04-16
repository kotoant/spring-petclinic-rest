package org.springframework.samples.petclinic.repository.r2dbc.coroutine

import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.context.annotation.Profile
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.samples.petclinic.model.Visit
import org.springframework.samples.petclinic.repository.DEFAULT_LAST_ID
import org.springframework.samples.petclinic.repository.DEFAULT_PAGE_SIZE
import org.springframework.samples.petclinic.repository.deleteVisitById
import org.springframework.samples.petclinic.repository.fetchAllVisitsReactive
import org.springframework.samples.petclinic.repository.fetchOneVisitByIdReactive
import org.springframework.samples.petclinic.repository.insertVisitReactive
import org.springframework.samples.petclinic.repository.r2dbc.inContextCoroutine
import org.springframework.samples.petclinic.repository.r2dbc.inContextCoroutineNullable
import org.springframework.samples.petclinic.repository.updateVisitReactive
import org.springframework.stereotype.Repository

@Repository
@Profile("r2dbc")
class CoroutineVisitRepository(private val client: DatabaseClient) {
    suspend fun fetchOneById(id: Int): Visit? =
        client.inContextCoroutineNullable { it.fetchOneVisitByIdReactive(id).awaitSingleOrNull() }

    suspend fun insert(visit: Visit): Visit = client.inContextCoroutine { it.insertVisitReactive(visit).awaitSingle() }
    suspend fun update(visit: Visit): Visit = client.inContextCoroutine { it.updateVisitReactive(visit).awaitSingle() }
    suspend fun fetchAll(lastId: Int = DEFAULT_LAST_ID, pageSize: Int = DEFAULT_PAGE_SIZE): List<Visit> =
        client.inContextCoroutine { it.fetchAllVisitsReactive(lastId, pageSize).awaitSingle() }

    suspend fun deleteById(id: Int): Boolean =
        client.inContextCoroutine { return@inContextCoroutine it.deleteVisitById(id).awaitSingle() > 0 }
}
