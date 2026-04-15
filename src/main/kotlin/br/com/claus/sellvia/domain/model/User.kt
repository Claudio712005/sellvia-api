package br.com.claus.sellvia.domain.model

import br.com.claus.sellvia.domain.enums.UserRole
import java.time.LocalDateTime

data class User(
    val id: Long? = null,
    val name: String = "",
    val cpf: String = "",
    val email: String = "",
    val username: String = "",
    val isActive: Boolean = true,
    val password: String = "",
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    val createdBy: String? = null,
    val updatedBy: String? = null,
    val role: UserRole,
    var company: Company? = null,
)