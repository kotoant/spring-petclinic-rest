package org.springframework.samples.petclinic.repository

import org.springframework.samples.petclinic.model.Owner

interface OwnerRepository {
    fun fetchOneById(id: Int): Owner?
    fun insert(owner: Owner): Owner
    fun update(owner: Owner): Owner
    fun fetchAll(lastId: Int? = DEFAULT_LAST_ID, pageSize: Int? = DEFAULT_PAGE_SIZE): List<Owner>
    fun fetchByLastName(
        lastName: String,
        lastId: Int? = DEFAULT_LAST_ID,
        pageSize: Int? = DEFAULT_PAGE_SIZE
    ): List<Owner>

    fun deleteById(id: Int): Boolean
}
