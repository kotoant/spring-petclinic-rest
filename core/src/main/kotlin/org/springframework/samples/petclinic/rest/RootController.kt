package org.springframework.samples.petclinic.rest

import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.SERVLET
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@ConditionalOnWebApplication(type = SERVLET)
class RootController {

    @Value("#{servletContext.contextPath}")
    private lateinit var servletContextPath: String

    @GetMapping(value = ["", "/"])
    fun redirectToSwagger(response: HttpServletResponse) {
        response.sendRedirect("${servletContextPath}/swagger-ui/index.html")
    }
}
