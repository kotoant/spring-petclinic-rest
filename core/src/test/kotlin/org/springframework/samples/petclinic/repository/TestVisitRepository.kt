package org.springframework.samples.petclinic.repository

import org.springframework.context.annotation.Profile
import org.springframework.samples.petclinic.model.Visit
import org.springframework.samples.petclinic.repository.r2dbc.R2dbcVisitRepository
import org.springframework.stereotype.Repository

@Repository
@Profile("r2dbc")
class TestVisitRepository(private val repository: R2dbcVisitRepository) : VisitRepository {
    override fun fetchOneById(id: Int): Visit? {
        return repository.fetchOneById(id).block()
    }

    override fun insert(visit: Visit): Visit {
        return repository.insert(visit).block()!!
    }

    override fun update(visit: Visit): Visit {
        return repository.update(visit).block()!!
    }

    override fun fetchAll(lastId: Int, pageSize: Int): List<Visit> {
        return repository.fetchAll(lastId, pageSize).block()!!
    }

    override fun deleteById(id: Int): Boolean {
        return repository.deleteById(id).block()!!
    }
}
