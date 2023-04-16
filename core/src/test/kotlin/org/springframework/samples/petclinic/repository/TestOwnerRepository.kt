package org.springframework.samples.petclinic.repository

import org.springframework.context.annotation.Profile
import org.springframework.samples.petclinic.model.Owner
import org.springframework.samples.petclinic.repository.r2dbc.R2dbcOwnerRepository
import org.springframework.stereotype.Repository

@Repository
@Profile("r2dbc")
class TestOwnerRepository(private val repository: R2dbcOwnerRepository) : OwnerRepository {
    override fun fetchOneById(id: Int): Owner? {
        return repository.fetchOneById(id).block()
    }

    override fun insert(owner: Owner): Owner {
        return repository.insert(owner).block()!!
    }

    override fun update(owner: Owner): Owner {
        return repository.update(owner).block()!!
    }

    override fun fetchAll(lastId: Int, pageSize: Int): List<Owner> {
        return repository.fetchAll(lastId, pageSize).block()!!
    }

    override fun fetchByLastName(lastName: String, lastId: Int, pageSize: Int): List<Owner> {
        return repository.fetchByLastName(lastName, lastId, pageSize).block()!!
    }

    override fun deleteById(id: Int): Boolean {
        return repository.deleteById(id).block()!!
    }
}
