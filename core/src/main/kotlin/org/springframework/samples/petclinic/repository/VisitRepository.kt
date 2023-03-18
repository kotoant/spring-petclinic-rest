package org.springframework.samples.petclinic.repository

import org.springframework.samples.petclinic.model.Visit

interface VisitRepository {
    fun fetchOneById(id: Int): Visit?
    fun insert(visit: Visit): Visit
    fun update(visit: Visit): Visit
    fun fetchAll(lastId: Int? = DEFAULT_LAST_ID, pageSize: Int? = DEFAULT_PAGE_SIZE): List<Visit>
    fun deleteById(id: Int): Boolean
}
