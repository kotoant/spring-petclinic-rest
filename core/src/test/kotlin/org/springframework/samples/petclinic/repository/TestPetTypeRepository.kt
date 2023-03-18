package org.springframework.samples.petclinic.repository

import org.springframework.context.annotation.Profile
import org.springframework.samples.petclinic.model.PetType
import org.springframework.samples.petclinic.repository.r2dbc.R2dbcPetTypeRepository
import org.springframework.stereotype.Repository

@Repository
@Profile("r2dbc")
class TestPetTypeRepository(private val repository: R2dbcPetTypeRepository) : PetTypeRepository {
    override fun fetchOneById(id: Int): PetType? {
        return repository.fetchOneById(id).block()
    }

    override fun insert(petType: PetType): PetType {
        return repository.insert(petType).block()!!
    }

    override fun update(petType: PetType): PetType {
        return repository.update(petType).block()!!
    }

    override fun fetchAll(lastId: Int?, pageSize: Int?): List<PetType> {
        return repository.fetchAll(lastId, pageSize).block()!!
    }

    override fun deleteById(id: Int): Boolean {
        return repository.deleteById(id).block()!!
    }
}
