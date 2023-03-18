package org.springframework.samples.petclinic.rest.webflux.jdbc.reactive

import org.springframework.samples.petclinic.rest.PetTypeControllerTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("webflux-jdbc-reactive")
class WebFluxJdbcReactivePetTypeControllerTest : PetTypeControllerTest()
