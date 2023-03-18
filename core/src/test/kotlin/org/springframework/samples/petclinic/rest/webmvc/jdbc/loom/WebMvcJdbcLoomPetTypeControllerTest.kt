package org.springframework.samples.petclinic.rest.webmvc.jdbc.loom

import org.springframework.samples.petclinic.rest.PetTypeControllerTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("webmvc-jdbc-loom")
class WebMvcJdbcLoomPetTypeControllerTest : PetTypeControllerTest()
