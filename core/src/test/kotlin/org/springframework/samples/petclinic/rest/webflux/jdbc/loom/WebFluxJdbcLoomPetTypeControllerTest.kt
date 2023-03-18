package org.springframework.samples.petclinic.rest.webflux.jdbc.loom

import org.springframework.samples.petclinic.rest.PetTypeControllerTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("webflux-jdbc-loom")
class WebFluxJdbcLoomPetTypeControllerTest : PetTypeControllerTest()
