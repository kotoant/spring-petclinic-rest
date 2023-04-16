package org.springframework.samples.petclinic.repository

import org.springframework.samples.petclinic.model.Pet

interface PetRepository {
    fun fetchOneById(id: Int): Pet?
    fun insert(pet: Pet): Pet
    fun update(pet: Pet): Pet
    fun fetchAll(lastId: Int = DEFAULT_LAST_ID, pageSize: Int = DEFAULT_PAGE_SIZE): List<Pet>
    fun deleteById(id: Int): Boolean
}
