package org.springframework.samples.petclinic.repository.r2dbc.coroutine

import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.context.annotation.Profile
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.samples.petclinic.model.PetType
import org.springframework.samples.petclinic.repository.DEFAULT_LAST_ID
import org.springframework.samples.petclinic.repository.DEFAULT_PAGE_SIZE
import org.springframework.samples.petclinic.repository.deletePetTypeById
import org.springframework.samples.petclinic.repository.deletePetsByPetTypeId
import org.springframework.samples.petclinic.repository.deleteVisitsByPetTypeId
import org.springframework.samples.petclinic.repository.fetchAllPetTypesReactive
import org.springframework.samples.petclinic.repository.fetchOnePetTypeByIdReactive
import org.springframework.samples.petclinic.repository.insertPetTypeReactive
import org.springframework.samples.petclinic.repository.r2dbc.inContextCoroutine
import org.springframework.samples.petclinic.repository.r2dbc.inContextCoroutineNullable
import org.springframework.samples.petclinic.repository.updatePetTypeReactive
import org.springframework.stereotype.Repository

@Repository
@Profile("r2dbc")
class CoroutinePetTypeRepository(private val client: DatabaseClient) {
    suspend fun fetchOneById(id: Int): PetType? =
        client.inContextCoroutineNullable { it.fetchOnePetTypeByIdReactive(id).awaitSingleOrNull() }

    suspend fun insert(petType: PetType): PetType =
        client.inContextCoroutine { it.insertPetTypeReactive(petType).awaitSingle() }

    suspend fun update(petType: PetType): PetType =
        client.inContextCoroutine { it.updatePetTypeReactive(petType).awaitSingle() }

    suspend fun fetchAll(lastId: Int = DEFAULT_LAST_ID, pageSize: Int = DEFAULT_PAGE_SIZE): List<PetType> =
        client.inContextCoroutine { it.fetchAllPetTypesReactive(lastId, pageSize).awaitSingle() }

    suspend fun deleteById(id: Int): Boolean = client.inContextCoroutine {
        it.deleteVisitsByPetTypeId(id).awaitSingle()
        it.deletePetsByPetTypeId(id).awaitSingle()
        return@inContextCoroutine it.deletePetTypeById(id).awaitSingle() > 0
    }
}
