package org.springframework.samples.petclinic.rest.reactive

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.REACTIVE
import org.springframework.boot.autoconfigure.web.reactive.WebFluxProperties
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.net.URI

@RestController
@ConditionalOnWebApplication(type = REACTIVE)
class ReactiveRootController(private val webFluxProperties: WebFluxProperties) {

    @GetMapping(value = ["", "/"])
    fun redirectToSwagger(response: ServerHttpResponse): Mono<Void> {
        response.statusCode = HttpStatus.PERMANENT_REDIRECT
        response.headers.location = URI.create("${webFluxProperties.basePath}/webjars/swagger-ui/index.html")
        return response.setComplete()
    }
}
