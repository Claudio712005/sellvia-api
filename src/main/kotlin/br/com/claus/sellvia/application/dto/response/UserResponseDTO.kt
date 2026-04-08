package br.com.claus.sellvia.application.dto.response

import br.com.claus.sellvia.domain.enums.UserRole

data class UserResponseDTO(
    val id: Long? = null,
    val username: String = "",
    val name: String = "",
    val role: UserRole,
    val email: String = "",
    val cpf: String = "",
)