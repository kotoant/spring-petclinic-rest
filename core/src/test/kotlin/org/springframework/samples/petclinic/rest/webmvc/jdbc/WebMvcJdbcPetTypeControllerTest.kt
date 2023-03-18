package org.springframework.samples.petclinic.rest.webmvc.jdbc

import org.springframework.samples.petclinic.rest.PetTypeControllerTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("webmvc-jdbc")
class WebMvcJdbcPetTypeControllerTest : PetTypeControllerTest()
