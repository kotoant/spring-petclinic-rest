package org.springframework.samples.petclinic.repository.jdbc

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.samples.petclinic.test.BaseTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("jdbc")
class JdbcSleepRepositoryTest : BaseTest() {
    @Autowired
    private lateinit var repository: JdbcSleepRepository

    @Test
    fun sleep() {
        val start = System.currentTimeMillis()
        repository.sleep(100)
        val elapsed = System.currentTimeMillis() - start
        Assertions.assertThat(elapsed).isGreaterThanOrEqualTo(100)
    }

    @Test
    fun sleepAndFetch() {
        val start = System.currentTimeMillis()
        repository.sleepAndFetch(100, 0, 0)
        val elapsed = System.currentTimeMillis() - start
        Assertions.assertThat(elapsed).isGreaterThanOrEqualTo(100)
    }
}
