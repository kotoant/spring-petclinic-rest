package org.springframework.samples.petclinic.repository

import org.springframework.context.annotation.Profile
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.repository.r2dbc.R2dbcPetRepository
import org.springframework.stereotype.Repository

@Repository
@Profile("r2dbc")
class TestPetRepository(private val repository: R2dbcPetRepository) : PetRepository {
    override fun fetchOneById(id: Int): Pet? {
        return repository.fetchOneById(id).block()
    }

    override fun insert(pet: Pet): Pet {
        return repository.insert(pet).block()!!
    }

    override fun update(pet: Pet): Pet {
        return repository.update(pet).block()!!
    }

    override fun fetchAll(lastId: Int, pageSize: Int): List<Pet> {
        return repository.fetchAll(lastId, pageSize).block()!!
    }

    override fun deleteById(id: Int): Boolean {
        return repository.deleteById(id).block()!!
    }
}
