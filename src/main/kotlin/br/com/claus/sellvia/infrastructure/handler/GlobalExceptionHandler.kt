package br.com.claus.sellvia.infrastructure.handler

import br.com.claus.sellvia.domain.exception.EntitiesConflictException
import br.com.claus.sellvia.domain.exception.InvalidCredentialsException
import br.com.claus.sellvia.domain.exception.InvalidFieldException
import br.com.claus.sellvia.domain.exception.InvalidTokenException
import br.com.claus.sellvia.domain.exception.NotFoundResouceException
import br.com.claus.sellvia.domain.exception.WithoutPermissionException
import br.com.claus.sellvia.domain.model.ErrorView
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ResponseStatusException

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationError(
        exception: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): ErrorView {
        val errorMessage = HashMap<String, String?>()
        exception.bindingResult.fieldErrors.forEach{
                e -> errorMessage.put(e.field, e.defaultMessage)
        }
        return ErrorView(
            status = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.name,
            message = errorMessage.toString(),
            path = request.servletPath
        )
    }

    @ExceptionHandler(NotFoundResouceException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFound(
        exception: NotFoundResouceException,
        request: HttpServletRequest
    ): ErrorView {
        return ErrorView(
            status = HttpStatus.NOT_FOUND.value(),
            error = HttpStatus.NOT_FOUND.name,
            message = exception.message,
            path = request.servletPath
        )
    }

    @ExceptionHandler(InvalidCredentialsException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun handleNotFound(
        exception: InvalidCredentialsException,
        request: HttpServletRequest
    ): ErrorView {
        return ErrorView(
            status = HttpStatus.FORBIDDEN.value(),
            error = HttpStatus.FORBIDDEN.name,
            message = exception.message,
            path = request.servletPath
        )
    }

    @ExceptionHandler(InvalidTokenException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleInvalidToken(
        exception: InvalidTokenException,
        request: HttpServletRequest
    ): ErrorView {
        return ErrorView(
            status = HttpStatus.UNAUTHORIZED.value(),
            error = HttpStatus.UNAUTHORIZED.name,
            message = exception.message,
            path = request.servletPath
        )
    }

    @ExceptionHandler(InvalidFieldException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleServerError(
        exception: InvalidFieldException,
        request: HttpServletRequest
    ): ErrorView {
        return ErrorView(
            status = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.name,
            message = exception.message,
            path = request.servletPath
        )
    }

    @ExceptionHandler(WithoutPermissionException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun handleServerError(
        exception: WithoutPermissionException,
        request: HttpServletRequest
    ): ErrorView {
        return ErrorView(
            status = HttpStatus.FORBIDDEN.value(),
            error = HttpStatus.FORBIDDEN.name,
            message = exception.message,
            path = request.servletPath
        )
    }

    @ExceptionHandler(EntitiesConflictException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleServerError(
        exception: EntitiesConflictException,
        request: HttpServletRequest
    ): ErrorView {
        return ErrorView(
            status = HttpStatus.CONFLICT.value(),
            error = HttpStatus.CONFLICT.name,
            message = exception.message,
            path = request.servletPath
        )
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleServerError(
        exception: Exception,
        request: HttpServletRequest
    ): ErrorView {
        return ErrorView(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = HttpStatus.INTERNAL_SERVER_ERROR.name,
            message = exception.message,
            path = request.servletPath
        )
    }
}

data class ErrorResponse(val message: String?)