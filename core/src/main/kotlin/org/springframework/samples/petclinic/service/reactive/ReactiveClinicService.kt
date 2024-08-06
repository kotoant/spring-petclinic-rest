package org.springframework.samples.petclinic.service.reactive

import org.springframework.samples.petclinic.model.Owner
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.model.PetType
import org.springframework.samples.petclinic.model.Visit
import org.springframework.samples.petclinic.repository.DEFAULT_LAST_ID
import org.springframework.samples.petclinic.repository.DEFAULT_PAGE_SIZE
import reactor.core.publisher.Mono

interface ReactiveClinicService {

    fun findPetById(id: Int): Mono<Pet>

    fun findAllPets(lastId: Int = DEFAULT_LAST_ID, pageSize: Int = DEFAULT_PAGE_SIZE): Mono<List<Pet>>

    fun savePet(pet: Pet): Mono<Pet>

    fun deletePet(id: Int): Mono<Boolean>

    fun findVisitById(id: Int): Mono<Visit>

    fun findAllVisits(lastId: Int = DEFAULT_LAST_ID, pageSize: Int = DEFAULT_PAGE_SIZE): Mono<List<Visit>>

    fun saveVisit(visit: Visit): Mono<Visit>

    fun deleteVisit(id: Int): Mono<Boolean>

    fun findOwnerById(id: Int): Mono<Owner>

    fun findAllOwners(lastId: Int = DEFAULT_LAST_ID, pageSize: Int = DEFAULT_PAGE_SIZE): Mono<List<Owner>>

    fun saveOwner(owner: Owner): Mono<Owner>

    fun deleteOwner(id: Int): Mono<Boolean>

    fun findOwnerByLastName(
        lastName: String,
        lastId: Int = DEFAULT_LAST_ID,
        pageSize: Int = DEFAULT_PAGE_SIZE
    ): Mono<List<Owner>>

    fun findPetTypeById(id: Int): Mono<PetType>

    fun findAllPetTypes(lastId: Int = DEFAULT_LAST_ID, pageSize: Int = DEFAULT_PAGE_SIZE): Mono<List<PetType>>

    fun savePetType(petType: PetType): Mono<PetType>

    fun deletePetType(id: Int): Mono<Boolean>

    fun sleep(times: Int, millis: Int, zip: Boolean): Mono<Unit>

    fun sleepAndFetchWithDb(times: Int, sleep: Boolean, millis: Int, strings: Int, length: Int, jooq: Boolean): Mono<List<String>>
    fun sleepAndFetchWithoutDb(times: Int, sleep: Boolean, millis: Int, strings: Int, length: Int): Mono<List<String>>
}
