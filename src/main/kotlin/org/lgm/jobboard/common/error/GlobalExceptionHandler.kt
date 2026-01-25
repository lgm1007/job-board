package org.lgm.jobboard.common.error

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException::class)
	fun handleValidation(e: MethodArgumentNotValidException): ResponseEntity<Map<String, Any>> {
		val errors = e.bindingResult.allErrors.mapNotNull { err ->
			if (err is FieldError) err.field to (err.defaultMessage ?: "invalid") else null
		}.toMap()

		return ResponseEntity.badRequest().body(
			mapOf(
				"message" to "Validation failed",
				"errors" to errors
			)
		)
	}

	@ExceptionHandler(IllegalArgumentException::class)
	fun handleIllegalArgument(e: IllegalArgumentException): ResponseEntity<Map<String, Any>> {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
			mapOf(
				"message" to (e.message ?: "Bad request")
			)
		)
	}
}