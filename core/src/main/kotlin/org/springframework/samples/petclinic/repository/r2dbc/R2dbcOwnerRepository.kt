package org.springframework.samples.petclinic.repository.r2dbc

import org.springframework.context.annotation.Profile
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.samples.petclinic.model.Owner
import org.springframework.samples.petclinic.repository.DEFAULT_LAST_ID
import org.springframework.samples.petclinic.repository.DEFAULT_PAGE_SIZE
import org.springframework.samples.petclinic.repository.deleteOwnerById
import org.springframework.samples.petclinic.repository.deletePetsByOwnerId
import org.springframework.samples.petclinic.repository.deleteVisitsByOwnerId
import org.springframework.samples.petclinic.repository.fetchAllOwnersReactive
import org.springframework.samples.petclinic.repository.fetchOneOwnerByIdReactive
import org.springframework.samples.petclinic.repository.fetchOwnersByLastNameReactive
import org.springframework.samples.petclinic.repository.insertOwnerReactive
import org.springframework.samples.petclinic.repository.updateOwnerReactive
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
@Profile("r2dbc")
class R2dbcOwnerRepository(private val client: DatabaseClient) {
    fun fetchOneById(id: Int): Mono<Owner> = client.inContext { it.fetchOneOwnerByIdReactive(id) }
    fun insert(owner: Owner): Mono<Owner> = client.inContext { it.insertOwnerReactive(owner) }
    fun update(owner: Owner): Mono<Owner> = client.inContext { it.updateOwnerReactive(owner) }
    fun fetchAll(lastId: Int = DEFAULT_LAST_ID, pageSize: Int = DEFAULT_PAGE_SIZE): Mono<List<Owner>> =
        client.inContext { it.fetchAllOwnersReactive(lastId, pageSize) }

    fun fetchByLastName(
        lastName: String,
        lastId: Int = DEFAULT_LAST_ID,
        pageSize: Int = DEFAULT_PAGE_SIZE
    ): Mono<List<Owner>> = client.inContext { it.fetchOwnersByLastNameReactive(lastName, lastId, pageSize) }

    fun deleteById(id: Int): Mono<Boolean> = client.inContext { ctx ->
        ctx.deleteVisitsByOwnerId(id)
            .then(ctx.deletePetsByOwnerId(id))
            .then(ctx.deleteOwnerById(id)).map { it > 0 }
    }
}
