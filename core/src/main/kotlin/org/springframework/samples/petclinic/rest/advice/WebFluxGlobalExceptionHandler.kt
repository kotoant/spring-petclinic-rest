package org.springframework.samples.petclinic.rest.advice

import jakarta.validation.ConstraintViolationException
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.REACTIVE
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@RestControllerAdvice
@ConditionalOnWebApplication(type = REACTIVE)
class WebFluxGlobalExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(
        ConstraintViolationException::class,
    )
    fun handleExceptions(ex: Exception, exchange: ServerWebExchange): Mono<ResponseEntity<Any>> {
        return when (ex) {
            is ConstraintViolationException -> handleConstraintViolationException(ex, exchange)
            else -> {
                if (logger.isWarnEnabled) {
                    logger.warn("Unexpected exception type: " + ex.javaClass.name)
                }
                Mono.error(ex)
            }
        }
    }

    private fun handleConstraintViolationException(
        ex: ConstraintViolationException,
        exchange: ServerWebExchange
    ): Mono<ResponseEntity<Any>> {
        val status = HttpStatus.BAD_REQUEST
        val body = ProblemDetail.forStatusAndDetail(status, ex.localizedMessage)
        return handleExceptionInternal(ex, body, null, status, exchange)
    }
}
