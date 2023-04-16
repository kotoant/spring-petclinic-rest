package org.springframework.samples.petclinic.rest.webflux.jdbc.loom

import org.springframework.samples.petclinic.rest.SleepControllerTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("webflux-jdbc-loom")
class WebFluxJdbcLoomSleepControllerTest : SleepControllerTest()
