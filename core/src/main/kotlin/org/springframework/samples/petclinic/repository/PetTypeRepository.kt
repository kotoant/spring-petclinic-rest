package org.springframework.samples.petclinic.repository

import org.springframework.samples.petclinic.model.PetType

interface PetTypeRepository {
    fun fetchOneById(id: Int): PetType?
    fun insert(petType: PetType): PetType
    fun update(petType: PetType): PetType
    fun fetchAll(lastId: Int? = DEFAULT_LAST_ID, pageSize: Int? = DEFAULT_PAGE_SIZE): List<PetType>
    fun deleteById(id: Int): Boolean
}
