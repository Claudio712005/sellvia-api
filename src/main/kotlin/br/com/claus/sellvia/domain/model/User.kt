package br.com.claus.sellvia.domain.model

import br.com.claus.sellvia.domain.enums.UserRole
import java.time.LocalDateTime

data class User(
    val id: Long? = null,
    val name: String,
    val cpf: String,
    val username: String,
    val isActive: Boolean,
    val password: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val createdBy: String =  "",
    val updatedBy: String = "",
    val role: UserRole,

    val company: Company? = null,
)
