package org.springframework.samples.petclinic.config

import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile

@Configuration
@Import(
    value = [
        SqlInitializationAutoConfiguration::class,
        R2dbcAutoConfiguration::class
    ]
)
@Profile("r2dbc")
class R2dbcConfig
