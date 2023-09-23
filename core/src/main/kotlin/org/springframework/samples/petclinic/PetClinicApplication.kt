package org.springframework.samples.petclinic

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(
    exclude = [
        DataSourceAutoConfiguration::class,
        R2dbcAutoConfiguration::class,
        WebSocketServletAutoConfiguration::class,
    ]
)
class PetClinicApplication

fun main(args: Array<String>) {
    runApplication<PetClinicApplication>(*args)
}
