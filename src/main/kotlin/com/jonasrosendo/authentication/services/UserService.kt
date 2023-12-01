package com.jonasrosendo.authentication.services

import com.jonasrosendo.authentication.repositories.UserRepository
import com.jonasrosendo.authentication.models.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun findByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }
}