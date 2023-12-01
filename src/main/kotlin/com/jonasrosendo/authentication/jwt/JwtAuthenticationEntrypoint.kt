package com.jonasrosendo.authentication.jwt

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint

class JwtAuthenticationEntrypoint : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        response?.setHeader("www-authenticate", "Bearer realm='/api/v1/auth'")
        response?.sendError(401)
    }
}