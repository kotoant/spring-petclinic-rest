package org.springframework.samples.petclinic.service.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.context.annotation.Profile
import org.springframework.samples.petclinic.model.Owner
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.model.PetType
import org.springframework.samples.petclinic.model.Visit
import org.springframework.samples.petclinic.service.JdbcClinicService
import org.springframework.stereotype.Service

@Service
@Profile("jdbc & coroutine")
class JdbcCoroutineClinicService(private val clinicService: JdbcClinicService) : CoroutineClinicService {

    private suspend fun <T> wrapBlockingCall(block: suspend CoroutineScope.() -> T): T =
        withContext(Dispatchers.IO, block)

    override suspend fun findPetById(id: Int): Pet = wrapBlockingCall {
        clinicService.findPetById(id)
    }

    override suspend fun findAllPets(lastId: Int, pageSize: Int): List<Pet> = wrapBlockingCall {
        clinicService.findAllPets(lastId, pageSize)
    }

    override suspend fun savePet(pet: Pet): Pet = wrapBlockingCall {
        clinicService.savePet(pet)
    }

    override suspend fun deletePet(id: Int): Boolean = wrapBlockingCall {
        clinicService.deletePet(id)
    }

    override suspend fun findVisitById(id: Int): Visit = wrapBlockingCall {
        clinicService.findVisitById(id)
    }

    override suspend fun findAllVisits(lastId: Int, pageSize: Int): List<Visit> = wrapBlockingCall {
        clinicService.findAllVisits(lastId, pageSize)
    }

    override suspend fun saveVisit(visit: Visit): Visit = wrapBlockingCall {
        clinicService.saveVisit(visit)
    }

    override suspend fun deleteVisit(id: Int): Boolean = wrapBlockingCall {
        clinicService.deleteVisit(id)
    }

    override suspend fun findOwnerById(id: Int): Owner = wrapBlockingCall {
        clinicService.findOwnerById(id)
    }

    override suspend fun findAllOwners(lastId: Int, pageSize: Int): List<Owner> = wrapBlockingCall {
        clinicService.findAllOwners(lastId, pageSize)
    }

    override suspend fun saveOwner(owner: Owner): Owner = wrapBlockingCall {
        clinicService.saveOwner(owner)
    }

    override suspend fun deleteOwner(id: Int): Boolean = wrapBlockingCall {
        clinicService.deleteOwner(id)
    }

    override suspend fun findOwnerByLastName(lastName: String, lastId: Int, pageSize: Int): List<Owner> =
        wrapBlockingCall {
            clinicService.findOwnerByLastName(lastName, lastId, pageSize)
        }

    override suspend fun findPetTypeById(id: Int): PetType = wrapBlockingCall {
        clinicService.findPetTypeById(id)
    }

    override suspend fun findAllPetTypes(lastId: Int, pageSize: Int): List<PetType> = wrapBlockingCall {
        clinicService.findAllPetTypes(lastId, pageSize)
    }

    override suspend fun savePetType(petType: PetType): PetType = wrapBlockingCall {
        clinicService.savePetType(petType)
    }

    override suspend fun deletePetType(id: Int): Boolean = wrapBlockingCall {
        clinicService.deletePetType(id)
    }

    override suspend fun sleep(times: Int, millis: Int) = wrapBlockingCall {
        clinicService.sleep(times, millis)
    }
}
