package com.jonasrosendo.authentication.exceptions

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError

data class ErrorMessage(
    val path: String,
    val method: String,
    val statusCode: Int,
    val statusText: String,
    val message: String,
    var errors: Map<String, String>? = null,
) {
    constructor(
        request: HttpServletRequest,
        statusCode: HttpStatus,
        message: String,
    ) : this(
        path = request.requestURI,
        method = request.method,
        statusCode = statusCode.value(),
        statusText = statusCode.reasonPhrase,
        message = message
    )

    constructor(
        request: HttpServletRequest,
        statusCode: HttpStatus,
        message: String,
        result: BindingResult,
    ) : this(
        path = request.requestURI,
        method = request.method,
        statusCode = statusCode.value(),
        statusText = statusCode.reasonPhrase,
        message = message
    ) { addErrors(result) }

    private fun addErrors(result: BindingResult) {
        errors = hashMapOf()
        for (fieldError: FieldError in result.fieldErrors) {
            (this.errors as HashMap<String, String>)[fieldError.field] = fieldError.defaultMessage.toString()
        }
    }
}