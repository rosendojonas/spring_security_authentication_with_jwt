package com.jonasrosendo.authentication.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.nio.charset.StandardCharsets
import java.security.Key
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.crypto.SecretKey

object JwtTokenGenerator {

    const val JWT_BEARER = "Bearer "
    const val JWT_AUTHORIZATION = "Authorization"
    private const val SECRET_KEY = "0123456789.0123456789.0123456789"
    private const val EXPIRE_DAYS = 0L
    private const val EXPIRE_HOURS = 0L
    private const val EXPIRE_MINUTES = 30L

    private fun generateKey(): Key {
        return Keys.hmacShaKeyFor(SECRET_KEY.toByteArray(StandardCharsets.UTF_8))
    }

    private fun toExpirationDate(start: Date): Date {
        val dateTime: LocalDateTime = start
            .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
        val end: LocalDateTime = dateTime
            .plusDays(EXPIRE_DAYS)
            .plusHours(EXPIRE_HOURS)
            .plusMinutes(EXPIRE_MINUTES)

        return Date.from(end.atZone(ZoneId.systemDefault()).toInstant())
    }

    fun createToken(username: String, role: String): JwtToken {
        val issuedAt = Date()
        val expiration = toExpirationDate(issuedAt)

        val token = Jwts.builder()
            .header().add("typ", "JWT")
            .and()
            .subject(username)
            .issuedAt(issuedAt)
            .expiration(expiration)
            .signWith(generateKey())
            .claim("role", role)
            .compact()

        return JwtToken(token)
    }

    private fun getClaimsFromToken(token: String): Claims? {
        return try {
            Jwts.parser()
                .verifyWith(generateKey() as SecretKey)
                .build()
                .parseSignedClaims(refactorToken(token)).payload
        } catch (e: JwtException) {
            e.printStackTrace()
            null
        }
    }

    private fun refactorToken(token: String): String {
        if (token.contains(JWT_BEARER)) {
            return token.substring(JWT_BEARER.length)
        }

        return token
    }

    fun isTokenValid(token: String): Boolean {
        try {
            Jwts.parser()
                .verifyWith(generateKey() as SecretKey)
                .build()
                .parseSignedClaims(refactorToken(token))
            return true
        } catch (e: JwtException) {
            e.printStackTrace()
        }

        return false
    }

    fun getUsernameFromToken(token: String): String? {
        return getClaimsFromToken(token)?.subject
    }
}