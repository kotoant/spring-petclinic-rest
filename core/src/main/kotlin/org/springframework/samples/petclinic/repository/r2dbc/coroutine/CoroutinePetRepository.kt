package org.springframework.samples.petclinic.repository.r2dbc.coroutine

import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.context.annotation.Profile
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.repository.DEFAULT_LAST_ID
import org.springframework.samples.petclinic.repository.DEFAULT_PAGE_SIZE
import org.springframework.samples.petclinic.repository.deletePetById
import org.springframework.samples.petclinic.repository.deleteVisitsByPetId
import org.springframework.samples.petclinic.repository.fetchAllPetsReactive
import org.springframework.samples.petclinic.repository.fetchOnePetByIdReactive
import org.springframework.samples.petclinic.repository.insertPetReactive
import org.springframework.samples.petclinic.repository.r2dbc.inContextCoroutine
import org.springframework.samples.petclinic.repository.r2dbc.inContextCoroutineNullable
import org.springframework.samples.petclinic.repository.updatePetReactive
import org.springframework.stereotype.Repository

@Repository
@Profile("r2dbc")
class CoroutinePetRepository(private val client: DatabaseClient) {
    suspend fun fetchOneById(id: Int): Pet? =
        client.inContextCoroutineNullable { it.fetchOnePetByIdReactive(id).awaitSingleOrNull() }

    suspend fun insert(pet: Pet): Pet = client.inContextCoroutine { it.insertPetReactive(pet).awaitSingle() }
    suspend fun update(pet: Pet): Pet = client.inContextCoroutine { it.updatePetReactive(pet).awaitSingle() }
    suspend fun fetchAll(lastId: Int? = DEFAULT_LAST_ID, pageSize: Int? = DEFAULT_PAGE_SIZE): List<Pet> =
        client.inContextCoroutine { it.fetchAllPetsReactive(lastId, pageSize).awaitSingle() }

    suspend fun deleteById(id: Int): Boolean = client.inContextCoroutine {
        it.deleteVisitsByPetId(id).awaitSingle()
        return@inContextCoroutine it.deletePetById(id).awaitSingle() > 0
    }
}
