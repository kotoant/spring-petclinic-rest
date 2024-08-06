package org.springframework.samples.petclinic.service

import io.micrometer.core.instrument.Metrics
import io.micrometer.core.instrument.Tags
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.samples.petclinic.model.Owner
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.model.PetType
import org.springframework.samples.petclinic.model.Visit
import org.springframework.stereotype.Service
import java.util.function.Supplier

@Primary
@Service
@Profile("jdbc")
class MonitoringJdbcClinicService(@Qualifier("jdbcClinicServiceImpl") private val service: JdbcClinicService) :
    JdbcClinicService {

    companion object {
        val tags = Tags.of("service.impl", "jdbc")
    }

    private fun <T> recordTimer(name: String, block: Supplier<T>): T {
        return Metrics.timer(name, tags).record(block)!!
    }

    override fun findPetById(id: Int): Pet = recordTimer("findPetById") { service.findPetById(id) }
    override fun findAllPets(lastId: Int, pageSize: Int): List<Pet> =
        recordTimer("findAllPets") { service.findAllPets(lastId, pageSize) }

    override fun savePet(pet: Pet): Pet = recordTimer("savePet") { service.savePet(pet) }
    override fun deletePet(id: Int): Boolean = recordTimer("deletePet") { service.deletePet(id) }
    override fun findVisitById(id: Int): Visit = recordTimer("findVisitById") { service.findVisitById(id) }
    override fun findAllVisits(lastId: Int, pageSize: Int): List<Visit> =
        recordTimer("findAllVisits") { service.findAllVisits(lastId, pageSize) }

    override fun saveVisit(visit: Visit): Visit = recordTimer("saveVisit") { service.saveVisit(visit) }
    override fun deleteVisit(id: Int): Boolean = recordTimer("deleteVisit") { service.deleteVisit(id) }
    override fun findOwnerById(id: Int): Owner = recordTimer("findOwnerById") { service.findOwnerById(id) }
    override fun findAllOwners(lastId: Int, pageSize: Int): List<Owner> =
        recordTimer("findAllOwners") { service.findAllOwners(lastId, pageSize) }

    override fun saveOwner(owner: Owner): Owner = recordTimer("saveOwner") { service.saveOwner(owner) }
    override fun deleteOwner(id: Int): Boolean = recordTimer("deleteOwner") { service.deleteOwner(id) }
    override fun findOwnerByLastName(lastName: String, lastId: Int, pageSize: Int): List<Owner> =
        recordTimer("findOwnerByLastName") { service.findOwnerByLastName(lastName, lastId, pageSize) }

    override fun findPetTypeById(id: Int): PetType = recordTimer("findPetTypeById") { service.findPetTypeById(id) }
    override fun findAllPetTypes(lastId: Int, pageSize: Int): List<PetType> =
        recordTimer("findAllPetTypes") { service.findAllPetTypes(lastId, pageSize) }

    override fun savePetType(petType: PetType): PetType = recordTimer("savePetType") { service.savePetType(petType) }
    override fun deletePetType(id: Int): Boolean = recordTimer("deletePetType") { service.deletePetType(id) }
    override fun sleep(times: Int, millis: Int) = recordTimer("sleep") { service.sleep(times, millis) }
    override fun sleepAndFetchWithDb(
        times: Int, sleep: Boolean, millis: Int, strings: Int, length: Int, jooq: Boolean
    ): List<String> =
        recordTimer("sleepAndFetchWithDb") { service.sleepAndFetchWithDb(times, sleep, millis, strings, length, jooq) }

    override fun sleepAndFetchWithoutDb(
        times: Int, sleep: Boolean, millis: Int, strings: Int, length: Int
    ): List<String> =
        recordTimer("sleepAndFetchWithoutDb") { service.sleepAndFetchWithoutDb(times, sleep, millis, strings, length) }
}
