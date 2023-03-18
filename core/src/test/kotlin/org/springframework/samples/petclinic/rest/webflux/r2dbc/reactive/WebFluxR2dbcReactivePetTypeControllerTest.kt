package org.springframework.samples.petclinic.rest.webflux.r2dbc.reactive

import org.springframework.samples.petclinic.rest.PetTypeControllerTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("webflux-r2dbc-reactive")
class WebFluxR2dbcReactivePetTypeControllerTest : PetTypeControllerTest()
