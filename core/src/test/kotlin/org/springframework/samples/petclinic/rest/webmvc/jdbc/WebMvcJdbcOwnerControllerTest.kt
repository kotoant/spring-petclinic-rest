package org.springframework.samples.petclinic.rest.webmvc.jdbc

import org.springframework.samples.petclinic.rest.OwnerControllerTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("webmvc-jdbc")
class WebMvcJdbcOwnerControllerTest : OwnerControllerTest()
