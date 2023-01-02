package code.seat.seatmowers.infrastructure.delivery.spring.controllers

import org.axonframework.modelling.command.AggregateNotFoundException
import org.springframework.hateoas.mediatype.problem.Problem
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionsHandlerController {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AggregateNotFoundException::class)
    fun handleAggregateNotFoundException(ex: AggregateNotFoundException): String = ex.message!!

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): Problem {
        val errors: MutableMap<String, Any> = HashMap()

        ex.bindingResult.allErrors.forEach { error ->
            val fieldName = (error as FieldError).field
            val errorMessage: String = error.getDefaultMessage()!!
            errors[fieldName] = errorMessage
        }

        return Problem.create()
            .withTitle("Invalid data")
            .withProperties(errors)
    }
}
