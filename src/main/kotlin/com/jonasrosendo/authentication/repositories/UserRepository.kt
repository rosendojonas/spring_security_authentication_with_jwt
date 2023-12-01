package com.jonasrosendo.authentication.repositories

import com.jonasrosendo.authentication.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String?): User?

    @Query("select u.role from User u where u.username like :username")
    fun findRoleByUsername(username: String): User.Role
}