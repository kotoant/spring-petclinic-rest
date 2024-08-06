package org.springframework.samples.petclinic.repository.r2dbc

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.samples.petclinic.test.BaseTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("r2dbc")
class R2dbcSleepRepositoryTest : BaseTest() {
    @Autowired
    private lateinit var repository: R2dbcSleepRepository

    @Test
    fun sleep() {
        val start = System.currentTimeMillis()
        repository.sleep(100).block()
        val elapsed = System.currentTimeMillis() - start
        Assertions.assertThat(elapsed).isGreaterThanOrEqualTo(100)
    }

    @ParameterizedTest
    @CsvSource(
        "true, true, true",
        "true, false, true",
        "true, false, false",
        "false, true, true",
        "false, false, true",
        "false, false, false",
    )
    fun sleepAndFetch(sleep: Boolean, jooq: Boolean, db: Boolean) {
        val start = System.currentTimeMillis()
        val strings = repository.sleepAndFetch(sleep, 100, 1000, 10, jooq, db).block()
        val elapsed = System.currentTimeMillis() - start
        if (sleep) {
            Assertions.assertThat(elapsed).isGreaterThanOrEqualTo(100)
        } else {
            Assertions.assertThat(elapsed).isLessThan(100)
        }
        Assertions.assertThat(strings).hasSize(1000)
        Assertions.assertThat(strings!![0]).hasSize(10)
    }
}
