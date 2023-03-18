package org.springframework.samples.petclinic.rest.webflux.jdbc.coroutine

import org.springframework.samples.petclinic.rest.OwnerControllerTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("webflux-jdbc-coroutine")
class WebFluxJdbcCoroutineOwnerControllerTest : OwnerControllerTest()
