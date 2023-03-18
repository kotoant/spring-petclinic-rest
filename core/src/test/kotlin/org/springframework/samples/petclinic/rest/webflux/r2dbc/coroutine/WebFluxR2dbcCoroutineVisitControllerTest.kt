package org.springframework.samples.petclinic.rest.webflux.r2dbc.coroutine

import org.springframework.samples.petclinic.rest.VisitControllerTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("webflux-r2dbc-coroutine")
class WebFluxR2dbcCoroutineVisitControllerTest : VisitControllerTest()
