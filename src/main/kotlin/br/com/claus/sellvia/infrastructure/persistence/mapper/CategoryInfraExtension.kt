package br.com.claus.sellvia.infrastructure.persistence.mapper

import br.com.claus.sellvia.domain.model.Category
import br.com.claus.sellvia.infrastructure.persistence.model.CategoryEntity
import br.com.claus.sellvia.infrastructure.persistence.model.CompanyEntity

fun Category.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = this.id,
        name = this.name!!,
        description = this.description!!,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        createdBy = this.createdBy,
        updatedBy = this.updatedBy
    ).also {
        it.company = CompanyEntity(id = this.company?.id)
    }
}

fun CategoryEntity.toDomain(): Category {
    return Category(
        id = this.id ?: 0L,
        name = this.name,
        description = this.description,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        createdBy = this.createdBy,
        updatedBy = this.updatedBy,
        company = this.company?.toDomain()
    )
}