package org.springframework.samples.petclinic.service.reactive

import org.springframework.context.annotation.Profile
import org.springframework.samples.petclinic.model.Owner
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.model.PetType
import org.springframework.samples.petclinic.model.Visit
import org.springframework.samples.petclinic.repository.r2dbc.R2dbcOwnerRepository
import org.springframework.samples.petclinic.repository.r2dbc.R2dbcPetRepository
import org.springframework.samples.petclinic.repository.r2dbc.R2dbcPetTypeRepository
import org.springframework.samples.petclinic.repository.r2dbc.R2dbcSleepRepository
import org.springframework.samples.petclinic.repository.r2dbc.R2dbcVisitRepository
import org.springframework.samples.petclinic.service.exception.OwnerNotFoundException
import org.springframework.samples.petclinic.service.exception.PetNotFoundException
import org.springframework.samples.petclinic.service.exception.PetTypeNotFoundException
import org.springframework.samples.petclinic.service.exception.VisitNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono

@Service
@Profile("r2dbc & reactive")
class R2dbcReactiveClinicService(
    private val ownerRepository: R2dbcOwnerRepository,
    private val petRepository: R2dbcPetRepository,
    private val petTypeRepository: R2dbcPetTypeRepository,
    private val visitRepository: R2dbcVisitRepository,
    private val sleepRepository: R2dbcSleepRepository,
) : ReactiveClinicService {

    @Transactional(transactionManager = "connectionFactoryTransactionManager", readOnly = true)
    override fun findPetById(id: Int): Mono<Pet> {
        return petRepository.fetchOneById(id).switchIfEmpty { Mono.error(PetNotFoundException(id)) }
    }

    @Transactional(transactionManager = "connectionFactoryTransactionManager", readOnly = true)
    override fun findAllPets(lastId: Int, pageSize: Int): Mono<List<Pet>> {
        return petRepository.fetchAll(lastId, pageSize)
    }

    @Transactional(transactionManager = "connectionFactoryTransactionManager")
    override fun savePet(pet: Pet): Mono<Pet> {
        return if (pet.id == 0) petRepository.insert(pet) else petRepository.update(pet)
    }

    @Transactional(transactionManager = "connectionFactoryTransactionManager")
    override fun deletePet(id: Int): Mono<Boolean> {
        return petRepository.deleteById(id)
    }

    @Transactional(transactionManager = "connectionFactoryTransactionManager", readOnly = true)
    override fun findVisitById(id: Int): Mono<Visit> {
        return visitRepository.fetchOneById(id).switchIfEmpty { Mono.error(VisitNotFoundException(id)) }
    }

    @Transactional(transactionManager = "connectionFactoryTransactionManager", readOnly = true)
    override fun findAllVisits(lastId: Int, pageSize: Int): Mono<List<Visit>> {
        return visitRepository.fetchAll(lastId, pageSize)
    }

    @Transactional(transactionManager = "connectionFactoryTransactionManager")
    override fun saveVisit(visit: Visit): Mono<Visit> {
        return if (visit.id == 0) visitRepository.insert(visit) else visitRepository.update(visit)
    }

    @Transactional(transactionManager = "connectionFactoryTransactionManager")
    override fun deleteVisit(id: Int): Mono<Boolean> {
        return visitRepository.deleteById(id)
    }

    @Transactional(transactionManager = "connectionFactoryTransactionManager", readOnly = true)
    override fun findOwnerById(id: Int): Mono<Owner> {
        return ownerRepository.fetchOneById(id).switchIfEmpty { Mono.error(OwnerNotFoundException(id)) }
    }

    @Transactional(transactionManager = "connectionFactoryTransactionManager", readOnly = true)
    override fun findAllOwners(lastId: Int, pageSize: Int): Mono<List<Owner>> {
        return ownerRepository.fetchAll(lastId, pageSize)
    }

    @Transactional(transactionManager = "connectionFactoryTransactionManager")
    override fun saveOwner(owner: Owner): Mono<Owner> {
        return if (owner.id == 0) ownerRepository.insert(owner) else ownerRepository.update(owner)
    }

    @Transactional(transactionManager = "connectionFactoryTransactionManager")
    override fun deleteOwner(id: Int): Mono<Boolean> {
        return ownerRepository.deleteById(id)
    }

    @Transactional(transactionManager = "connectionFactoryTransactionManager", readOnly = true)
    override fun findOwnerByLastName(lastName: String, lastId: Int, pageSize: Int): Mono<List<Owner>> {
        return ownerRepository.fetchByLastName(lastName, lastId, pageSize)
    }

    @Transactional(transactionManager = "connectionFactoryTransactionManager", readOnly = true)
    override fun findPetTypeById(id: Int): Mono<PetType> {
        return petTypeRepository.fetchOneById(id).switchIfEmpty { Mono.error(PetTypeNotFoundException(id)) }
    }

    @Transactional(transactionManager = "connectionFactoryTransactionManager", readOnly = true)
    override fun findAllPetTypes(lastId: Int, pageSize: Int): Mono<List<PetType>> {
        return petTypeRepository.fetchAll(lastId, pageSize)
    }

    @Transactional(transactionManager = "connectionFactoryTransactionManager")
    override fun savePetType(petType: PetType): Mono<PetType> {
        return if (petType.id == 0) petTypeRepository.insert(petType) else petTypeRepository.update(petType)
    }

    @Transactional(transactionManager = "connectionFactoryTransactionManager")
    override fun deletePetType(id: Int): Mono<Boolean> {
        return petTypeRepository.deleteById(id)
    }

    @Transactional(transactionManager = "connectionFactoryTransactionManager", readOnly = true)
    override fun sleep(times: Int, millis: Int, zip: Boolean): Mono<Unit> {
        return if (zip) {
            Mono.zip(List(times) { sleepRepository.sleep(millis) }) {}
        } else {
            sleepRepository.sleep(millis).repeat(times - 1L).then().then(Unit.toMono())
        }
    }

    @Transactional(transactionManager = "connectionFactoryTransactionManager", readOnly = true)
    override fun sleepAndFetchWithDb(
        times: Int, sleep: Boolean, millis: Int, strings: Int, length: Int, jooq: Boolean
    ): Mono<List<String>> {
        return sleepRepository.sleepAndFetch(sleep, millis, strings, length, jooq, db = true).repeat(times - 1L).last()
    }

    override fun sleepAndFetchWithoutDb(
        times: Int, sleep: Boolean, millis: Int, strings: Int, length: Int
    ): Mono<List<String>> {
        return sleepRepository.sleepAndFetch(sleep, millis, strings, length, jooq = false, db = false)
            .repeat(times - 1L).last()
    }
}
