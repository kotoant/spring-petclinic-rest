package org.springframework.samples.petclinic.repository.jdbc

import org.jooq.DSLContext
import org.springframework.context.annotation.Profile
import org.springframework.samples.petclinic.model.Visit
import org.springframework.samples.petclinic.repository.VisitRepository
import org.springframework.samples.petclinic.repository.deleteVisitById
import org.springframework.samples.petclinic.repository.fetchAllVisits
import org.springframework.samples.petclinic.repository.fetchOneVisitById
import org.springframework.samples.petclinic.repository.insertVisit
import org.springframework.samples.petclinic.repository.updateVisit
import org.springframework.stereotype.Repository

@Repository
@Profile("jdbc")
class JdbcVisitRepository(private val ctx: DSLContext) : VisitRepository {
    override fun fetchOneById(id: Int): Visit? = ctx.fetchOneVisitById(id)
    override fun insert(visit: Visit): Visit = ctx.insertVisit(visit)
    override fun update(visit: Visit): Visit = ctx.updateVisit(visit)
    override fun fetchAll(lastId: Int?, pageSize: Int?): List<Visit> = ctx.fetchAllVisits(lastId, pageSize)
    override fun deleteById(id: Int): Boolean {
        return ctx.deleteVisitById(id).execute() > 0
    }
}
