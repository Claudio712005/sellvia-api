package br.com.claus.sellvia.infrastructure.persistence.mapper

import br.com.claus.sellvia.application.dto.response.UserResponseDTO
import br.com.claus.sellvia.domain.model.User
import br.com.claus.sellvia.infrastructure.persistence.model.UserEntity

fun User.toEntity(): UserEntity {
    return UserEntity(
        username = this.username,
        password = this.password,
        createdBy = this.createdBy,
        updatedBy = this.updatedBy,
        id = this.id ?: 0,
        cpf = this.cpf,
        updatedAt = this.updatedAt,
        createdAt = this.createdAt,
        role = this.role,
        name = this.name,
        isActive = this.isActive
    )
}

fun User.toResponseDTO(): UserResponseDTO {
    return UserResponseDTO(
        username = this.username,
        id = this.id ?: 0,
        role = this.role,
        name = this.name,
    )
}

fun UserEntity.toModel(): User {
    return User(
        username = this.username,
        password = this.password,
        createdBy = this.createdBy,
        updatedBy = this.updatedBy,
        id = this.id,
        cpf = this.cpf,
        updatedAt = this.updatedAt,
        createdAt = this.createdAt,
        role = this.role,
        name = this.name,
        isActive = this.isActive,
        company = company?.toDomain()
    )
}