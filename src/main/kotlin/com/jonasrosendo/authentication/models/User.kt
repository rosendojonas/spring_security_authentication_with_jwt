package com.jonasrosendo.authentication.models

import jakarta.persistence.*
import org.hibernate.validator.constraints.br.CPF
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener::class)
data class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "username", length = 100, unique = true, nullable = false)
    val username: String,

    @Column(name = "password", length = 100, nullable = false)
    val password: String,

    @Column(name = "role", nullable = false, length = 25)
    @Enumerated(value = EnumType.STRING)
    val role: Role,

    @Column(name = "creation_date")
    @CreatedDate
    val creationDate: LocalDateTime,

    @Column(name = "modification_date")
    @LastModifiedDate
    val modificationDate: LocalDateTime,

    @Column(name = "created_by")
    @CreatedBy
    val createdBy: LocalDateTime,

    @Column(name = "modified_by")
    @LastModifiedBy
    val modifiedBy: LocalDateTime
) {
    enum class Role {
        ROLE_ADMIN,
        ROLE_USER
    }
}