package org.springframework.samples.petclinic.repository

interface SleepRepository {
    fun sleep(millis: Int)

    fun sleepAndFetch(sleep: Boolean, millis: Int, strings: Int, length: Int, jooq: Boolean, db: Boolean): List<String>
}
