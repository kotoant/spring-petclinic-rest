package org.springframework.samples.petclinic.repository.r2dbc

import org.springframework.context.annotation.Profile
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.samples.petclinic.model.Visit
import org.springframework.samples.petclinic.repository.DEFAULT_LAST_ID
import org.springframework.samples.petclinic.repository.DEFAULT_PAGE_SIZE
import org.springframework.samples.petclinic.repository.deleteVisitById
import org.springframework.samples.petclinic.repository.fetchAllVisitsReactive
import org.springframework.samples.petclinic.repository.fetchOneVisitByIdReactive
import org.springframework.samples.petclinic.repository.insertVisitReactive
import org.springframework.samples.petclinic.repository.updateVisitReactive
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
@Profile("r2dbc")
class R2dbcVisitRepository(private val client: DatabaseClient) {
    fun fetchOneById(id: Int): Mono<Visit> = client.inContext { it.fetchOneVisitByIdReactive(id) }
    fun insert(visit: Visit): Mono<Visit> = client.inContext { it.insertVisitReactive(visit) }
    fun update(visit: Visit): Mono<Visit> = client.inContext { it.updateVisitReactive(visit) }
    fun fetchAll(lastId: Int? = DEFAULT_LAST_ID, pageSize: Int? = DEFAULT_PAGE_SIZE): Mono<List<Visit>> =
        client.inContext { it.fetchAllVisitsReactive(lastId, pageSize) }

    fun deleteById(id: Int): Mono<Boolean> = client.inContext { ctx -> ctx.deleteVisitById(id).map { it > 0 } }
}
