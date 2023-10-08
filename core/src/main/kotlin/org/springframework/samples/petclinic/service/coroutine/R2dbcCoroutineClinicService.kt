package org.springframework.samples.petclinic.service.coroutine

import org.springframework.context.annotation.Profile
import org.springframework.samples.petclinic.model.Owner
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.model.PetType
import org.springframework.samples.petclinic.model.Visit
import org.springframework.samples.petclinic.repository.r2dbc.coroutine.*
import org.springframework.samples.petclinic.service.exception.OwnerNotFoundException
import org.springframework.samples.petclinic.service.exception.PetNotFoundException
import org.springframework.samples.petclinic.service.exception.PetTypeNotFoundException
import org.springframework.samples.petclinic.service.exception.VisitNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Profile("r2dbc & coroutine")
class R2dbcCoroutineClinicService(
    private val ownerRepository: CoroutineOwnerRepository,
    private val petRepository: CoroutinePetRepository,
    private val petTypeRepository: CoroutinePetTypeRepository,
    private val visitRepository: CoroutineVisitRepository,
    private val sleepRepository: CoroutineSleepRepository,
) : CoroutineClinicService {

    @Transactional(transactionManager = "connectionFactoryTransactionManager", readOnly = true)
    override suspend fun findPetById(id: Int): Pet {
        return petRepository.fetchOneById(id) ?: throw PetNotFoundException(id)
    }

    @Transactional(transactionManager = "connectionFactoryTransactionManager", readOnly = true)
    override suspend fun findAllPets(lastId: Int, pageSize: Int): List<Pet> {
        return petRepository.fetchAll(lastId, pageSize)
    }

    @Transactional(transactionManager = "connectionFactoryTransactionManager")
    override suspend fun savePet(pet: Pet): Pet {
        return if (pet.id == 0) petRepository.insert(pet) else petRepository.update(pet)
    }

    @Transactional(transactionManager = "connectionFactoryTransactionManager")
    override suspend fun deletePet(id: Int): Boolean {
        return petRepository.deleteById(id)
    }

    @Transactional(transactionManager = "connectionFactoryTransactionManager", readOnly = true)
    override suspend fun findVisitById(id: Int): Visit {
        return visitRepository.fetchOneById(id) ?: throw VisitNotFoundException(id)
    }

    @Transactional(transactionManager = "connectionFactoryTransactionManager", readOnly = true)
    override suspend fun findAllVisits(lastId: Int, pageSize: Int): List<Visit> {
        return visitRepository.fetchAll(lastId, pageSize)
    }

    @Transactional(transactionManager = "connectionFactoryTransactionManager")
    override suspend fun saveVisit(visit: Visit): Visit {
        return if (visit.id == 0) visitRepository.insert(visit) else visitRepository.update(visit)
    }

    @Transactional(transactionManager = "connectionFactoryTransactionManager")
    override suspend fun deleteVisit(id: Int): Boolean {
        return visitRepository.deleteById(id)
    }

    @Transactional(transactionManager = "connectionFactoryTransactionManager", readOnly = true)
    override suspend fun findOwnerById(id: Int): Owner {
        return ownerRepository.fetchOneById(id) ?: throw OwnerNotFoundException(id)
    }

    @Transactional(transactionManager = "connectionFactoryTransactionManager", readOnly = true)
    override suspend fun findAllOwners(lastId: Int, pageSize: Int): List<Owner> {
        return ownerRepository.fetchAll(lastId, pageSize)
    }

    @Transactional(transactionManager = "connectionFactoryTransactionManager")
    override suspend fun saveOwner(owner: Owner): Owner {
        return if (owner.id == 0) ownerRepository.insert(owner) else ownerRepository.update(owner)
    }

    @Transactional(transactionManager = "connectionFactoryTransactionManager")
    override suspend fun deleteOwner(id: Int): Boolean {
        return ownerRepository.deleteById(id)
    }

    @Transactional(transactionManager = "connectionFactoryTransactionManager", readOnly = true)
    override suspend fun findOwnerByLastName(lastName: String, lastId: Int, pageSize: Int): List<Owner> {
        return ownerRepository.fetchByLastName(lastName, lastId, pageSize)
    }

    @Transactional(transactionManager = "connectionFactoryTransactionManager", readOnly = true)
    override suspend fun findPetTypeById(id: Int): PetType {
        return petTypeRepository.fetchOneById(id) ?: throw PetTypeNotFoundException(id)
    }

    @Transactional(transactionManager = "connectionFactoryTransactionManager", readOnly = true)
    override suspend fun findAllPetTypes(lastId: Int, pageSize: Int): List<PetType> {
        return petTypeRepository.fetchAll(lastId, pageSize)
    }

    @Transactional(transactionManager = "connectionFactoryTransactionManager")
    override suspend fun savePetType(petType: PetType): PetType {
        return if (petType.id == 0) petTypeRepository.insert(petType) else petTypeRepository.update(petType)
    }

    @Transactional(transactionManager = "connectionFactoryTransactionManager")
    override suspend fun deletePetType(id: Int): Boolean {
        return petTypeRepository.deleteById(id)
    }

    @Transactional(transactionManager = "connectionFactoryTransactionManager", readOnly = true)
    override suspend fun sleep(times: Int, millis: Int) {
        for (i in 1..times) {
            sleepRepository.sleep(millis)
        }
    }
}
