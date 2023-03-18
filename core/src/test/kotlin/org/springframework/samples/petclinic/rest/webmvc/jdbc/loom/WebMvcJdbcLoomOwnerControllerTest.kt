package org.springframework.samples.petclinic.rest.webmvc.jdbc.loom

import org.springframework.samples.petclinic.rest.OwnerControllerTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("webmvc-jdbc-loom")
class WebMvcJdbcLoomOwnerControllerTest : OwnerControllerTest()
