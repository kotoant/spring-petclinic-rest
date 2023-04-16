package org.springframework.samples.petclinic.repository.jdbc

import org.jooq.DSLContext
import org.springframework.context.annotation.Profile
import org.springframework.samples.petclinic.model.PetType
import org.springframework.samples.petclinic.repository.PetTypeRepository
import org.springframework.samples.petclinic.repository.deletePetTypeById
import org.springframework.samples.petclinic.repository.deletePetsByPetTypeId
import org.springframework.samples.petclinic.repository.deleteVisitsByPetTypeId
import org.springframework.samples.petclinic.repository.fetchAllPetTypes
import org.springframework.samples.petclinic.repository.fetchOnePetTypeById
import org.springframework.samples.petclinic.repository.insertPetType
import org.springframework.samples.petclinic.repository.updatePetType
import org.springframework.stereotype.Repository

@Repository
@Profile("jdbc")
class JdbcPetTypeRepository(private val ctx: DSLContext) : PetTypeRepository {
    override fun fetchOneById(id: Int): PetType? = ctx.fetchOnePetTypeById(id)
    override fun insert(petType: PetType): PetType = ctx.insertPetType(petType)
    override fun update(petType: PetType): PetType = ctx.updatePetType(petType)
    override fun fetchAll(lastId: Int, pageSize: Int): List<PetType> = ctx.fetchAllPetTypes(lastId, pageSize)
    override fun deleteById(id: Int): Boolean {
        ctx.deleteVisitsByPetTypeId(id).execute()
        ctx.deletePetsByPetTypeId(id).execute()
        return ctx.deletePetTypeById(id).execute() > 0
    }
}
