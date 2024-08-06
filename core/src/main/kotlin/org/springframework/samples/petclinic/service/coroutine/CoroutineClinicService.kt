package org.springframework.samples.petclinic.service.coroutine

import org.springframework.samples.petclinic.model.Owner
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.model.PetType
import org.springframework.samples.petclinic.model.Visit
import org.springframework.samples.petclinic.repository.DEFAULT_LAST_ID
import org.springframework.samples.petclinic.repository.DEFAULT_PAGE_SIZE

interface CoroutineClinicService {

    suspend fun findPetById(id: Int): Pet

    suspend fun findAllPets(lastId: Int = DEFAULT_LAST_ID, pageSize: Int = DEFAULT_PAGE_SIZE): List<Pet>

    suspend fun savePet(pet: Pet): Pet

    suspend fun deletePet(id: Int): Boolean

    suspend fun findVisitById(id: Int): Visit

    suspend fun findAllVisits(lastId: Int = DEFAULT_LAST_ID, pageSize: Int = DEFAULT_PAGE_SIZE): List<Visit>

    suspend fun saveVisit(visit: Visit): Visit

    suspend fun deleteVisit(id: Int): Boolean

    suspend fun findOwnerById(id: Int): Owner

    suspend fun findAllOwners(lastId: Int = DEFAULT_LAST_ID, pageSize: Int = DEFAULT_PAGE_SIZE): List<Owner>

    suspend fun saveOwner(owner: Owner): Owner

    suspend fun deleteOwner(id: Int): Boolean

    suspend fun findOwnerByLastName(
        lastName: String,
        lastId: Int = DEFAULT_LAST_ID,
        pageSize: Int = DEFAULT_PAGE_SIZE
    ): List<Owner>

    suspend fun findPetTypeById(id: Int): PetType

    suspend fun findAllPetTypes(lastId: Int = DEFAULT_LAST_ID, pageSize: Int = DEFAULT_PAGE_SIZE): List<PetType>

    suspend fun savePetType(petType: PetType): PetType

    suspend fun deletePetType(id: Int): Boolean

    suspend fun sleep(times: Int, millis: Int, zip: Boolean)

    suspend fun sleepAndFetchWithDb(times: Int, sleep: Boolean, millis: Int, strings: Int, length: Int, jooq: Boolean): List<String>
    suspend fun sleepAndFetchWithoutDb(times: Int, sleep: Boolean, millis: Int, strings: Int, length: Int): List<String>
}
