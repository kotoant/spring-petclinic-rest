package org.springframework.samples.petclinic.repository.r2dbc

import org.springframework.context.annotation.Profile
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.repository.*
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
@Profile("r2dbc")
class R2dbcPetRepository(private val client: DatabaseClient) {
    fun fetchOneById(id: Int): Mono<Pet> = client.inContext { it.fetchOnePetByIdReactive(id) }
    fun insert(pet: Pet): Mono<Pet> = client.inContext { it.insertPetReactive(pet) }
    fun update(pet: Pet): Mono<Pet> = client.inContext { it.updatePetReactive(pet) }
    fun fetchAll(lastId: Int = DEFAULT_LAST_ID, pageSize: Int = DEFAULT_PAGE_SIZE): Mono<List<Pet>> =
        client.inContext { it.fetchAllPetsReactive(lastId, pageSize) }

    fun deleteById(id: Int): Mono<Boolean> = client.inContext { ctx ->
        Mono.zip(
            ctx.deleteVisitsByPetId(id).toMono(),
            ctx.deletePetById(id).toMono()
        ).map { it.t2 > 0 }
    }
}
