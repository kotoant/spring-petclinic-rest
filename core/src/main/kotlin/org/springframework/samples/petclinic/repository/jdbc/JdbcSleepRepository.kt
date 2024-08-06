package org.springframework.samples.petclinic.repository.jdbc

import org.jooq.DSLContext
import org.springframework.context.annotation.Profile
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.samples.petclinic.repository.SleepRepository
import org.springframework.samples.petclinic.repository.sleep
import org.springframework.samples.petclinic.repository.sleepAndFetch
import org.springframework.samples.petclinic.util.Util
import org.springframework.stereotype.Repository
import javax.sql.DataSource

@Repository
@Profile("jdbc")
class JdbcSleepRepository(private val ctx: DSLContext, dataSource: DataSource) : SleepRepository {
    private val jdbcTemplate = NamedParameterJdbcTemplate(dataSource)
    override fun sleep(millis: Int): Unit = ctx.sleep(millis)
    override fun sleepAndFetch(
        sleep: Boolean, millis: Int, strings: Int, length: Int, jooq: Boolean, db: Boolean
    ): List<String> =
        if (db) {
            if (jooq) ctx.sleepAndFetch(sleep, millis, strings, length)
            else jdbcTemplate.sleepAndFetch(sleep, millis, strings, length)
        } else {
            if (sleep) {
                Thread.sleep(millis.toLong())
            }
            Util.getRandomStrings(strings, length)
        }
}
