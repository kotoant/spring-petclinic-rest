package org.springframework.samples.petclinic.service.coroutine

import io.micrometer.core.instrument.Metrics
import io.micrometer.core.instrument.Tags
import io.micrometer.core.instrument.Timer
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.samples.petclinic.model.Owner
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.model.PetType
import org.springframework.samples.petclinic.model.Visit
import org.springframework.stereotype.Service

@Primary
@Service
@Profile("r2dbc & coroutine")
class MonitoringR2dbcCoroutineClinicService(
    private val service: R2dbcCoroutineClinicService,
) : CoroutineClinicService {
    companion object {
        val tags = Tags.of("service.impl", "r2dbc-coroutine")
    }

    private suspend fun <T> recordTimer(name: String, block: suspend () -> T): T {
        val sample = Timer.start()
        val result = block.invoke()
        sample.stop(Metrics.timer(name, tags))
        return result
    }

    override suspend fun findPetById(id: Int): Pet = recordTimer("findPetById") { service.findPetById(id) }
    override suspend fun findAllPets(lastId: Int, pageSize: Int): List<Pet> =
        recordTimer("findAllPets") { service.findAllPets(lastId, pageSize) }

    override suspend fun savePet(pet: Pet): Pet = recordTimer("savePet") { service.savePet(pet) }
    override suspend fun deletePet(id: Int): Boolean = recordTimer("deletePet") { service.deletePet(id) }
    override suspend fun findVisitById(id: Int): Visit = recordTimer("findVisitById") { service.findVisitById(id) }
    override suspend fun findAllVisits(lastId: Int, pageSize: Int): List<Visit> = recordTimer("findAllVisits") {
        service.findAllVisits(lastId, pageSize)
    }

    override suspend fun saveVisit(visit: Visit): Visit = recordTimer("saveVisit") { service.saveVisit(visit) }
    override suspend fun deleteVisit(id: Int): Boolean = recordTimer("deleteVisit") { service.deleteVisit(id) }
    override suspend fun findOwnerById(id: Int): Owner = recordTimer("findOwnerById") { service.findOwnerById(id) }
    override suspend fun findAllOwners(lastId: Int, pageSize: Int): List<Owner> =
        recordTimer("findAllOwners") { service.findAllOwners(lastId, pageSize) }

    override suspend fun saveOwner(owner: Owner): Owner = recordTimer("saveOwner") { service.saveOwner(owner) }
    override suspend fun deleteOwner(id: Int): Boolean = recordTimer("deleteOwner") { service.deleteOwner(id) }
    override suspend fun findOwnerByLastName(lastName: String, lastId: Int, pageSize: Int): List<Owner> =
        recordTimer("findOwnerByLastName") { service.findOwnerByLastName(lastName, lastId, pageSize) }

    override suspend fun findPetTypeById(id: Int): PetType =
        recordTimer("findPetTypeById") { service.findPetTypeById(id) }

    override suspend fun findAllPetTypes(lastId: Int, pageSize: Int): List<PetType> =
        recordTimer("findAllPetTypes") { service.findAllPetTypes(lastId, pageSize) }

    override suspend fun savePetType(petType: PetType): PetType =
        recordTimer("savePetType") { service.savePetType(petType) }

    override suspend fun deletePetType(id: Int): Boolean = recordTimer("deletePetType") { service.deletePetType(id) }
    override suspend fun sleep(times: Int, millis: Int, zip: Boolean) =
        recordTimer("sleep") { service.sleep(times, millis, zip) }

    override suspend fun sleepAndFetchWithDb(
        times: Int, sleep: Boolean, millis: Int, strings: Int, length: Int, jooq: Boolean
    ) = recordTimer("sleepAndFetch") { service.sleepAndFetchWithDb(times, sleep, millis, strings, length, jooq) }

    override suspend fun sleepAndFetchWithoutDb(times: Int, sleep: Boolean, millis: Int, strings: Int, length: Int) =
        recordTimer("sleepAndFetchWithoutDb") { service.sleepAndFetchWithoutDb(times, sleep, millis, strings, length) }
}
