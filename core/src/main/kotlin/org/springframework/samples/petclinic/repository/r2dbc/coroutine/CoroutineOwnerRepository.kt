package org.springframework.samples.petclinic.repository.r2dbc.coroutine

import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
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
import org.springframework.samples.petclinic.repository.r2dbc.inContextCoroutine
import org.springframework.samples.petclinic.repository.r2dbc.inContextCoroutineNullable
import org.springframework.samples.petclinic.repository.updateOwnerReactive
import org.springframework.stereotype.Repository

@Repository
@Profile("r2dbc")
class CoroutineOwnerRepository(private val client: DatabaseClient) {
    suspend fun fetchOneById(id: Int): Owner? =
        client.inContextCoroutineNullable { it.fetchOneOwnerByIdReactive(id).awaitSingleOrNull() }

    suspend fun insert(owner: Owner): Owner = client.inContextCoroutine { it.insertOwnerReactive(owner).awaitSingle() }
    suspend fun update(owner: Owner): Owner = client.inContextCoroutine { it.updateOwnerReactive(owner).awaitSingle() }
    suspend fun fetchAll(lastId: Int? = DEFAULT_LAST_ID, pageSize: Int? = DEFAULT_PAGE_SIZE): List<Owner> =
        client.inContextCoroutine { it.fetchAllOwnersReactive(lastId, pageSize).awaitSingle() }

    suspend fun fetchByLastName(
        lastName: String,
        lastId: Int? = DEFAULT_LAST_ID,
        pageSize: Int? = DEFAULT_PAGE_SIZE
    ): List<Owner> =
        client.inContextCoroutine { it.fetchOwnersByLastNameReactive(lastName, lastId, pageSize).awaitSingle() }

    suspend fun deleteById(id: Int): Boolean = client.inContextCoroutine {
        it.deleteVisitsByOwnerId(id).awaitSingle()
        it.deletePetsByOwnerId(id).awaitSingle()
        return@inContextCoroutine it.deleteOwnerById(id).awaitSingle() > 0
    }
}