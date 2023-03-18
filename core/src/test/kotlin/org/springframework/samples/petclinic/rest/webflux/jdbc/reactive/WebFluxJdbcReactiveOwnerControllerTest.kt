package org.springframework.samples.petclinic.rest.webflux.jdbc.reactive

import org.springframework.samples.petclinic.rest.OwnerControllerTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("webflux-jdbc-reactive")
class WebFluxJdbcReactiveOwnerControllerTest : OwnerControllerTest()
