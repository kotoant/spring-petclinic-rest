package org.springframework.samples.petclinic.repository.r2dbc

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
import org.springframework.samples.petclinic.repository.updatePetTypeReactive
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
@Profile("r2dbc")
class R2dbcPetTypeRepository(private val client: DatabaseClient) {
    fun fetchOneById(id: Int): Mono<PetType> = client.inContext { it.fetchOnePetTypeByIdReactive(id) }
    fun insert(petType: PetType): Mono<PetType> = client.inContext { it.insertPetTypeReactive(petType) }
    fun update(petType: PetType): Mono<PetType> = client.inContext { it.updatePetTypeReactive(petType) }
    fun fetchAll(lastId: Int = DEFAULT_LAST_ID, pageSize: Int = DEFAULT_PAGE_SIZE): Mono<List<PetType>> =
        client.inContext { it.fetchAllPetTypesReactive(lastId, pageSize) }

    fun deleteById(id: Int): Mono<Boolean> = client.inContext { ctx ->
        ctx.deleteVisitsByPetTypeId(id)
            .then(ctx.deletePetsByPetTypeId(id))
            .then(ctx.deletePetTypeById(id)).map { it > 0 }
    }
}
