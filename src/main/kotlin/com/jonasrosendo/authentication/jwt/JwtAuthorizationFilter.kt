package com.jonasrosendo.authentication.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


@Component
class JwtAuthorizationFilter(
    private val userDetailsService: JwtUserDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val token = request.getHeader(JwtTokenGenerator.JWT_AUTHORIZATION)

        if (token == null || !token.startsWith(JwtTokenGenerator.JWT_BEARER)) {
            logger.info("Jwt token é nulo ou vazio ou não foi inicializado com 'Bearer '.")
            filterChain.doFilter(request, response)
            return
        }

        if (!JwtTokenGenerator.isTokenValid(token)) {
            logger.warn("Jwt token é inválido ou está expirado.")
            filterChain.doFilter(request, response)
            return
        }

        val username = JwtTokenGenerator.getUsernameFromToken(token)

        authenticate(request, username)

        filterChain.doFilter(request, response)

    }

    private fun authenticate(request: HttpServletRequest, username: String?) {

        val userDetails: UserDetails = userDetailsService.loadUserByUsername(username)
        val authenticationToken = UsernamePasswordAuthenticationToken
            .authenticated(userDetails, null, userDetails.authorities)

        authenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authenticationToken
    }
}