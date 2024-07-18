package org.springframework.samples.petclinic.repository

interface SleepRepository {
    fun sleep(millis: Int)

    fun sleepAndFetch(millis: Int, string: Int, length: Int): List<String>
}
