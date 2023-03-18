package org.springframework.samples.petclinic.service.reactive

import org.springframework.context.annotation.Profile
import org.springframework.samples.petclinic.model.Owner
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.model.PetType
import org.springframework.samples.petclinic.model.Visit
import org.springframework.samples.petclinic.service.JdbcClinicService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Scheduler
import reactor.kotlin.core.publisher.toMono
import java.util.concurrent.Callable

@Service
@Profile("jdbc & reactive ")
class JdbcReactiveClinicService(private val clinicService: JdbcClinicService, private val scheduler: Scheduler) :
    ReactiveClinicService {

    private fun <T> wrapBlockingCall(block: Callable<T?>): Mono<T> {
        return block.toMono().subscribeOn(scheduler)
    }

    override fun findPetById(id: Int): Mono<Pet> = wrapBlockingCall {
        clinicService.findPetById(id)
    }

    override fun findAllPets(lastId: Int?, pageSize: Int?): Mono<List<Pet>> = wrapBlockingCall {
        clinicService.findAllPets(lastId, pageSize)
    }

    override fun savePet(pet: Pet): Mono<Pet> = wrapBlockingCall {
        clinicService.savePet(pet)
    }

    override fun deletePet(id: Int): Mono<Boolean> = wrapBlockingCall {
        clinicService.deletePet(id)
    }

    override fun findVisitById(id: Int): Mono<Visit> = wrapBlockingCall {
        clinicService.findVisitById(id)
    }

    override fun findAllVisits(lastId: Int?, pageSize: Int?): Mono<List<Visit>> = wrapBlockingCall {
        clinicService.findAllVisits(lastId, pageSize)
    }

    override fun saveVisit(visit: Visit): Mono<Visit> = wrapBlockingCall {
        clinicService.saveVisit(visit)
    }

    override fun deleteVisit(id: Int): Mono<Boolean> = wrapBlockingCall {
        clinicService.deleteVisit(id)
    }

    override fun findOwnerById(id: Int): Mono<Owner> = wrapBlockingCall {
        clinicService.findOwnerById(id)
    }

    override fun findAllOwners(lastId: Int?, pageSize: Int?): Mono<List<Owner>> = wrapBlockingCall {
        clinicService.findAllOwners(lastId, pageSize)
    }

    override fun saveOwner(owner: Owner): Mono<Owner> = wrapBlockingCall {
        clinicService.saveOwner(owner)
    }

    override fun deleteOwner(id: Int): Mono<Boolean> = wrapBlockingCall {
        clinicService.deleteOwner(id)
    }

    override fun findOwnerByLastName(lastName: String, lastId: Int?, pageSize: Int?): Mono<List<Owner>> =
        wrapBlockingCall {
            clinicService.findOwnerByLastName(lastName, lastId, pageSize)
        }

    override fun findPetTypeById(id: Int): Mono<PetType> = wrapBlockingCall {
        clinicService.findPetTypeById(id)
    }

    override fun findAllPetTypes(lastId: Int?, pageSize: Int?): Mono<List<PetType>> = wrapBlockingCall {
        clinicService.findAllPetTypes(lastId, pageSize)
    }

    override fun savePetType(petType: PetType): Mono<PetType> = wrapBlockingCall {
        clinicService.savePetType(petType)
    }

    override fun deletePetType(id: Int): Mono<Boolean> = wrapBlockingCall {
        clinicService.deletePetType(id)
    }
}