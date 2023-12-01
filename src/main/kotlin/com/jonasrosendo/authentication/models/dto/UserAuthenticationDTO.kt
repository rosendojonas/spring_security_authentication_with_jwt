package com.jonasrosendo.authentication.models.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UserAuthenticationDTO(

    @NotBlank
    @Email(
        message = "E-mail inválido",
        regexp = "^[a-z0-9.+-]+@[a-z0-9.-]+\\.[a-z]{2,}$"
    )
    val email: String,
    @Size(min = 8)
    val password: String
)
