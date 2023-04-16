package org.springframework.samples.petclinic.service

import org.springframework.context.annotation.Profile
import org.springframework.samples.petclinic.model.Owner
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.model.PetType
import org.springframework.samples.petclinic.model.Visit
import org.springframework.samples.petclinic.repository.jdbc.JdbcOwnerRepository
import org.springframework.samples.petclinic.repository.jdbc.JdbcPetRepository
import org.springframework.samples.petclinic.repository.jdbc.JdbcPetTypeRepository
import org.springframework.samples.petclinic.repository.jdbc.JdbcSleepRepository
import org.springframework.samples.petclinic.repository.jdbc.JdbcVisitRepository
import org.springframework.samples.petclinic.service.exception.OwnerNotFoundException
import org.springframework.samples.petclinic.service.exception.PetNotFoundException
import org.springframework.samples.petclinic.service.exception.PetTypeNotFoundException
import org.springframework.samples.petclinic.service.exception.VisitNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Profile("jdbc")
class JdbcClinicServiceImpl(
    private val ownerRepository: JdbcOwnerRepository,
    private val petRepository: JdbcPetRepository,
    private val petTypeRepository: JdbcPetTypeRepository,
    private val visitRepository: JdbcVisitRepository,
    private val sleepRepository: JdbcSleepRepository,
) : JdbcClinicService {

    @Transactional(transactionManager = "transactionManager", readOnly = true)
    override fun findPetById(id: Int): Pet {
        return petRepository.fetchOneById(id) ?: throw PetNotFoundException(id)
    }

    @Transactional(transactionManager = "transactionManager", readOnly = true)
    override fun findAllPets(lastId: Int, pageSize: Int): List<Pet> {
        return petRepository.fetchAll(lastId, pageSize)
    }

    @Transactional(transactionManager = "transactionManager")
    override fun savePet(pet: Pet): Pet {
        return if (pet.id == 0) petRepository.insert(pet) else petRepository.update(pet)
    }

    @Transactional(transactionManager = "transactionManager")
    override fun deletePet(id: Int): Boolean {
        return petRepository.deleteById(id)
    }

    @Transactional(transactionManager = "transactionManager", readOnly = true)
    override fun findVisitById(id: Int): Visit {
        return visitRepository.fetchOneById(id) ?: throw VisitNotFoundException(id)
    }

    @Transactional(transactionManager = "transactionManager", readOnly = true)
    override fun findAllVisits(lastId: Int, pageSize: Int): List<Visit> {
        return visitRepository.fetchAll(lastId, pageSize)
    }

    @Transactional(transactionManager = "transactionManager")
    override fun saveVisit(visit: Visit): Visit {
        return if (visit.id == 0) visitRepository.insert(visit) else visitRepository.update(visit)
    }

    @Transactional(transactionManager = "transactionManager")
    override fun deleteVisit(id: Int): Boolean {
        return visitRepository.deleteById(id)
    }

    @Transactional(transactionManager = "transactionManager", readOnly = true)
    override fun findOwnerById(id: Int): Owner {
        return ownerRepository.fetchOneById(id) ?: throw OwnerNotFoundException(id)
    }

    @Transactional(transactionManager = "transactionManager", readOnly = true)
    override fun findAllOwners(lastId: Int, pageSize: Int): List<Owner> {
        return ownerRepository.fetchAll(lastId, pageSize)
    }

    @Transactional(transactionManager = "transactionManager")
    override fun saveOwner(owner: Owner): Owner {
        return if (owner.id == 0) ownerRepository.insert(owner) else ownerRepository.update(owner)
    }

    @Transactional(transactionManager = "transactionManager")
    override fun deleteOwner(id: Int): Boolean {
        return ownerRepository.deleteById(id)
    }

    @Transactional(transactionManager = "transactionManager", readOnly = true)
    override fun findOwnerByLastName(lastName: String, lastId: Int, pageSize: Int): List<Owner> {
        return ownerRepository.fetchByLastName(lastName, lastId, pageSize)
    }

    @Transactional(transactionManager = "transactionManager", readOnly = true)
    override fun findPetTypeById(id: Int): PetType {
        return petTypeRepository.fetchOneById(id) ?: throw PetTypeNotFoundException(id)
    }

    @Transactional(transactionManager = "transactionManager", readOnly = true)
    override fun findAllPetTypes(lastId: Int, pageSize: Int): List<PetType> {
        return petTypeRepository.fetchAll(lastId, pageSize)
    }

    @Transactional(transactionManager = "transactionManager")
    override fun savePetType(petType: PetType): PetType {
        return if (petType.id == 0) petTypeRepository.insert(petType) else petTypeRepository.update(petType)
    }

    @Transactional(transactionManager = "transactionManager")
    override fun deletePetType(id: Int): Boolean {
        return petTypeRepository.deleteById(id)
    }

    @Transactional(transactionManager = "transactionManager", readOnly = true)
    override fun sleep(times: Int, millis: Int) {
        for (i in 1..times) {
            sleepRepository.sleep(millis)
        }
    }
}
