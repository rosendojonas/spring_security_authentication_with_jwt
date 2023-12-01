package com.jonasrosendo.authentication.controllers

import com.jonasrosendo.authentication.exceptions.ErrorMessage
import com.jonasrosendo.authentication.jwt.JwtUserDetailsService
import com.jonasrosendo.authentication.models.dto.UserAuthenticationDTO
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthenticationController(
    private val jwtUserDetailsService: JwtUserDetailsService,
    private val authenticationManager: AuthenticationManager,
) {

    @PostMapping
    fun authenticate(
        @RequestBody @Valid userAuthenticationDTO: UserAuthenticationDTO,
        request: HttpServletRequest,
    ): ResponseEntity<*> {
        try {
            val usernamePasswordAuthenticationToken =
                UsernamePasswordAuthenticationToken(
                    userAuthenticationDTO.email,
                    userAuthenticationDTO.password
                )

            authenticationManager.authenticate(usernamePasswordAuthenticationToken)
            val token = jwtUserDetailsService.getAuthenticatedToken(userAuthenticationDTO.email)
            return ResponseEntity.ok(token)
        } catch (e: AuthenticationException) {
            e.printStackTrace()
        }

        return ResponseEntity
            .badRequest()
            .body(
                ErrorMessage(
                    request = request,
                    statusCode = HttpStatus.BAD_REQUEST,
                    "Credenciais inválidas."
                )
            )
    }
}