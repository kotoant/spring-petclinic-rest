package org.springframework.samples.petclinic.service.reactive

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
import reactor.core.publisher.Mono

@Primary
@Service
@Profile("r2dbc & reactive")
class MonitoringR2dbcReactiveClinicService(
    private val service: R2dbcReactiveClinicService
) : ReactiveClinicService {

    companion object {
        val tags = Tags.of("service.impl", "r2dbc-reactive")
    }

    private fun <T> recordTimer(name: String, block: Mono<T>): Mono<T> {
        val sample = Timer.start()
        return block.doOnTerminate { sample.stop(Metrics.timer(name, tags)) }
    }

    override fun findPetById(id: Int): Mono<Pet> = recordTimer("findPetById", service.findPetById(id))
    override fun findAllPets(lastId: Int, pageSize: Int): Mono<List<Pet>> =
        recordTimer("findAllPets", service.findAllPets(lastId, pageSize))

    override fun savePet(pet: Pet): Mono<Pet> = recordTimer("savePet", service.savePet(pet))
    override fun deletePet(id: Int): Mono<Boolean> = recordTimer("deletePet", service.deletePet(id))
    override fun findVisitById(id: Int): Mono<Visit> = recordTimer("findVisitById", service.findVisitById(id))
    override fun findAllVisits(lastId: Int, pageSize: Int): Mono<List<Visit>> =
        recordTimer("findAllVisits", service.findAllVisits(lastId, pageSize))

    override fun saveVisit(visit: Visit): Mono<Visit> = recordTimer("saveVisit", service.saveVisit(visit))
    override fun deleteVisit(id: Int): Mono<Boolean> = recordTimer("deleteVisit", service.deleteVisit(id))
    override fun findOwnerById(id: Int): Mono<Owner> = recordTimer("findOwnerById", service.findOwnerById(id))
    override fun findAllOwners(lastId: Int, pageSize: Int): Mono<List<Owner>> =
        recordTimer("findAllOwners", service.findAllOwners(lastId, pageSize))

    override fun saveOwner(owner: Owner): Mono<Owner> = recordTimer("saveOwner", service.saveOwner(owner))
    override fun deleteOwner(id: Int): Mono<Boolean> = recordTimer("deleteOwner", service.deleteOwner(id))
    override fun findOwnerByLastName(lastName: String, lastId: Int, pageSize: Int): Mono<List<Owner>> =
        recordTimer("findOwnerByLastName", service.findOwnerByLastName(lastName, lastId, pageSize))

    override fun findPetTypeById(id: Int): Mono<PetType> = recordTimer("findPetTypeById", service.findPetTypeById(id))
    override fun findAllPetTypes(lastId: Int, pageSize: Int): Mono<List<PetType>> =
        recordTimer("findAllPetTypes", service.findAllPetTypes(lastId, pageSize))

    override fun savePetType(petType: PetType): Mono<PetType> = recordTimer("savePetType", service.savePetType(petType))
    override fun deletePetType(id: Int): Mono<Boolean> = recordTimer("deletePetType", service.deletePetType(id))
    override fun sleep(times: Int, millis: Int, zip: Boolean): Mono<Unit> = recordTimer("sleep", service.sleep(times, millis, zip))
}
