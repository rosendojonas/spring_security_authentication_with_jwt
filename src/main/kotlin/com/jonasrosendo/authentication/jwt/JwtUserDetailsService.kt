package com.jonasrosendo.authentication.jwt

import com.jonasrosendo.authentication.repositories.UserRepository
import com.jonasrosendo.authentication.models.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class JwtUserDetailsService(
    private val userRepository: UserRepository,
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        val user: User? = userRepository.findByUsername(username)
        return if (user != null) JwtUserDetails(user)
        else throw UsernameNotFoundException("Não foi possível encontrar usuário='$username'")
    }

    fun getAuthenticatedToken(username: String): JwtToken {
        val role: User.Role = userRepository.findRoleByUsername(username)
        return JwtTokenGenerator.createToken(username, role.name.substring("ROLE_".length))
    }
}