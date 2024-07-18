package org.springframework.samples.petclinic.repository.r2dbc.coroutine

import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.samples.petclinic.test.BaseTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("r2dbc")
class CoroutineSleepRepositoryTest : BaseTest() {

    @Autowired
    private lateinit var repository: CoroutineSleepRepository

    @Test
    fun sleep() {
        runBlocking {
            val start = System.currentTimeMillis()
            repository.sleep(100)
            val elapsed = System.currentTimeMillis() - start
            Assertions.assertThat(elapsed).isGreaterThanOrEqualTo(100)
        }
    }

    @Test
    fun sleepAndFetch() {
        runBlocking {
            val start = System.currentTimeMillis()
            repository.sleepAndFetch(100, 0, 0)
            val elapsed = System.currentTimeMillis() - start
            Assertions.assertThat(elapsed).isGreaterThanOrEqualTo(100)
        }
    }
}
