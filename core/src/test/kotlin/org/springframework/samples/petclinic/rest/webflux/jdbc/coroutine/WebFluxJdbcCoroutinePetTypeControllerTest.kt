package org.springframework.samples.petclinic.rest.webflux.jdbc.coroutine

import org.springframework.samples.petclinic.rest.PetTypeControllerTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("webflux-jdbc-coroutine")
class WebFluxJdbcCoroutinePetTypeControllerTest : PetTypeControllerTest()
