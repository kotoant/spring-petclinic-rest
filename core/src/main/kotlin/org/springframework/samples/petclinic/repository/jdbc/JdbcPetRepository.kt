package org.springframework.samples.petclinic.repository.jdbc

import org.jooq.DSLContext
import org.springframework.context.annotation.Profile
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.repository.PetRepository
import org.springframework.samples.petclinic.repository.deletePetById
import org.springframework.samples.petclinic.repository.deleteVisitsByPetId
import org.springframework.samples.petclinic.repository.fetchAllPets
import org.springframework.samples.petclinic.repository.fetchOnePetById
import org.springframework.samples.petclinic.repository.insertPet
import org.springframework.samples.petclinic.repository.updatePet
import org.springframework.stereotype.Repository

@Repository
@Profile("jdbc")
class JdbcPetRepository(private val ctx: DSLContext) : PetRepository {
    override fun fetchOneById(id: Int): Pet? = ctx.fetchOnePetById(id)
    override fun insert(pet: Pet): Pet = ctx.insertPet(pet)
    override fun update(pet: Pet): Pet = ctx.updatePet(pet)
    override fun fetchAll(lastId: Int, pageSize: Int): List<Pet> = ctx.fetchAllPets(lastId, pageSize)
    override fun deleteById(id: Int): Boolean {
        ctx.deleteVisitsByPetId(id).execute()
        return ctx.deletePetById(id).execute() > 0
    }
}
