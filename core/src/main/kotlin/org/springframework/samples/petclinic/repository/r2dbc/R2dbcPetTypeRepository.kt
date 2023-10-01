package org.springframework.samples.petclinic.repository.r2dbc

import org.springframework.context.annotation.Profile
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.samples.petclinic.model.PetType
import org.springframework.samples.petclinic.repository.*
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
        Mono.zip(
            ctx.deleteVisitsByPetTypeId(id).toMono(),
            ctx.deletePetsByPetTypeId(id).toMono(),
            ctx.deletePetTypeById(id).toMono()
        ).map { it.t3 > 0 }
    }
}
