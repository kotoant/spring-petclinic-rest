package org.springframework.samples.petclinic.service

import org.springframework.samples.petclinic.model.Owner
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.model.PetType
import org.springframework.samples.petclinic.model.Visit
import org.springframework.samples.petclinic.repository.DEFAULT_LAST_ID
import org.springframework.samples.petclinic.repository.DEFAULT_PAGE_SIZE

interface JdbcClinicService {

    fun findPetById(id: Int): Pet

    fun findAllPets(lastId: Int = DEFAULT_LAST_ID, pageSize: Int = DEFAULT_PAGE_SIZE): List<Pet>

    fun savePet(pet: Pet): Pet

    fun deletePet(id: Int): Boolean

    fun findVisitById(id: Int): Visit

    fun findAllVisits(lastId: Int = DEFAULT_LAST_ID, pageSize: Int = DEFAULT_PAGE_SIZE): List<Visit>

    fun saveVisit(visit: Visit): Visit

    fun deleteVisit(id: Int): Boolean

    fun findOwnerById(id: Int): Owner

    fun findAllOwners(lastId: Int = DEFAULT_LAST_ID, pageSize: Int = DEFAULT_PAGE_SIZE): List<Owner>

    fun saveOwner(owner: Owner): Owner

    fun deleteOwner(id: Int): Boolean

    fun findOwnerByLastName(
        lastName: String,
        lastId: Int = DEFAULT_LAST_ID,
        pageSize: Int = DEFAULT_PAGE_SIZE
    ): List<Owner>

    fun findPetTypeById(id: Int): PetType

    fun findAllPetTypes(lastId: Int = DEFAULT_LAST_ID, pageSize: Int = DEFAULT_PAGE_SIZE): List<PetType>

    fun savePetType(petType: PetType): PetType

    fun deletePetType(id: Int): Boolean

    fun sleep(times: Int, millis: Int)
}
