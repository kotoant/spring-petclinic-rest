package org.springframework.samples.petclinic.repository.jdbc

import org.jooq.DSLContext
import org.springframework.context.annotation.Profile
import org.springframework.samples.petclinic.model.Owner
import org.springframework.samples.petclinic.repository.OwnerRepository
import org.springframework.samples.petclinic.repository.deleteOwnerById
import org.springframework.samples.petclinic.repository.deletePetsByOwnerId
import org.springframework.samples.petclinic.repository.deleteVisitsByOwnerId
import org.springframework.samples.petclinic.repository.fetchAllOwners
import org.springframework.samples.petclinic.repository.fetchOneOwnerById
import org.springframework.samples.petclinic.repository.fetchOwnersByLastName
import org.springframework.samples.petclinic.repository.insertOwner
import org.springframework.samples.petclinic.repository.updateOwner
import org.springframework.stereotype.Repository

@Repository
@Profile("jdbc")
class JdbcOwnerRepository(private val ctx: DSLContext) : OwnerRepository {
    override fun fetchOneById(id: Int): Owner? = ctx.fetchOneOwnerById(id)
    override fun insert(owner: Owner): Owner = ctx.insertOwner(owner)
    override fun update(owner: Owner): Owner = ctx.updateOwner(owner)
    override fun fetchAll(lastId: Int?, pageSize: Int?): List<Owner> =
        ctx.fetchAllOwners(lastId, pageSize)

    override fun fetchByLastName(lastName: String, lastId: Int?, pageSize: Int?): List<Owner> =
        ctx.fetchOwnersByLastName(lastName, lastId, pageSize)

    override fun deleteById(id: Int): Boolean {
        ctx.deleteVisitsByOwnerId(id).execute()
        ctx.deletePetsByOwnerId(id).execute()
        return ctx.deleteOwnerById(id).execute() > 0
    }
}
