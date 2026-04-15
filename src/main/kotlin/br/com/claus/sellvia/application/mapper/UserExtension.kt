package br.com.claus.sellvia.application.mapper

import br.com.claus.sellvia.application.dto.request.UserRequestDTO
import br.com.claus.sellvia.application.dto.response.UserResponseDTO
import br.com.claus.sellvia.domain.enums.UserRole
import br.com.claus.sellvia.domain.model.Company
import br.com.claus.sellvia.domain.model.User

fun User.toResponseDTO(): UserResponseDTO {
    return UserResponseDTO(
        username = this.username,
        id = this.id ?: 0,
        role = this.role,
        name = this.name,
        email = this.email,
        cpf = this.cpf,
    )
}

fun UserRequestDTO.toUser(): User {
    return User(
        id,
        name ?: "",
        cpf ?: "",
        email ?: "",
        username ?: "",
        true,
        password ?: "",
        null,
        null,
        null,
        null,
        userRole ?: UserRole.COMPANY_USER,
        Company(
            companyId,
        )
    )
}