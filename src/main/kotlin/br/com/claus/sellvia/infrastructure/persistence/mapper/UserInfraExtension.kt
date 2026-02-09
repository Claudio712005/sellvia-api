package br.com.claus.sellvia.infrastructure.persistence.mapper

import br.com.claus.sellvia.application.dto.request.UserRequestDTO
import br.com.claus.sellvia.domain.model.User
import br.com.claus.sellvia.infrastructure.persistence.model.UserEntity

fun User.toEntity(): UserEntity {
    val entity = UserEntity(
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
        isActive = this.isActive,
        email = this.email,
    )

    entity.company = this.company?.toEntity()
    return entity
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
        company = company?.toDomain(),
        email = this.email,
    )
}