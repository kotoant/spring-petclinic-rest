package org.springframework.samples.petclinic.repository.jdbc

import org.jooq.DSLContext
import org.springframework.context.annotation.Profile
import org.springframework.samples.petclinic.repository.SleepRepository
import org.springframework.samples.petclinic.repository.sleep
import org.springframework.stereotype.Repository

@Repository
@Profile("jdbc")
class JdbcSleepRepository(private val ctx: DSLContext) : SleepRepository {
    override fun sleep(millis: Int): Unit = ctx.sleep(millis)
}
