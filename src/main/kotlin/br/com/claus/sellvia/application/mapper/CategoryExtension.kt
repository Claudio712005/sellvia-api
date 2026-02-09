package br.com.claus.sellvia.application.mapper

import br.com.claus.sellvia.application.dto.request.CategoryRequestDTO
import br.com.claus.sellvia.domain.model.Category
import br.com.claus.sellvia.domain.model.Company

fun CategoryRequestDTO.toDomain(): Category {
    return Category(
        id = this.id,
        name = this.name,
        description = this.description,
        company = Company(
            id = this.companyId
        ),
        createdAt = null,
        updatedAt = null,
        createdBy = null,
        updatedBy = null,
    )
}