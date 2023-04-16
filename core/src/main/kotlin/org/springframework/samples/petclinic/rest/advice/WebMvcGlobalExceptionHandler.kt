package org.springframework.samples.petclinic.rest.advice

import jakarta.validation.ConstraintViolationException
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.SERVLET
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.lang.Nullable
import org.springframework.samples.petclinic.service.exception.NotFoundException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
@ConditionalOnWebApplication(type = SERVLET)
class WebMvcGlobalExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(
        ConstraintViolationException::class,
        NotFoundException::class,
    )
    @Nullable
    @Throws(Exception::class)
    fun handleExceptions(ex: Exception, request: WebRequest): ResponseEntity<Any>? {
        return when (ex) {
            is ConstraintViolationException -> handleConstraintViolationException(ex, request)
            is NotFoundException -> handleNotFoundException(ex, request)
            else -> {
                if (logger.isWarnEnabled) {
                    logger.warn("Unexpected exception type: " + ex.javaClass.name)
                }
                throw ex
            }
        }
    }

    @Nullable
    protected fun handleConstraintViolationException(
        ex: ConstraintViolationException, request: WebRequest
    ): ResponseEntity<Any>? {
        val status = HttpStatus.BAD_REQUEST
        val body = ProblemDetail.forStatusAndDetail(status, ex.localizedMessage)
        return handleExceptionInternal(ex, body, HttpHeaders.EMPTY, status, request)
    }

    @Nullable
    protected fun handleNotFoundException(
        ex: NotFoundException, request: WebRequest
    ): ResponseEntity<Any>? {
        val status = HttpStatus.NOT_FOUND
        val body = ProblemDetail.forStatusAndDetail(status, ex.localizedMessage)
        return handleExceptionInternal(ex, body, HttpHeaders.EMPTY, status, request)
    }
}
