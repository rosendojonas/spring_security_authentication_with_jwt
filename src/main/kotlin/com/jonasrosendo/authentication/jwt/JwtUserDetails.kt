package com.jonasrosendo.authentication.jwt

import com.jonasrosendo.authentication.models.User
import org.springframework.security.core.authority.AuthorityUtils

data class JwtUserDetails(
    val user: User,
) : org.springframework.security.core.userdetails.User
    (user.username, user.password, AuthorityUtils.createAuthorityList(user.role.name))