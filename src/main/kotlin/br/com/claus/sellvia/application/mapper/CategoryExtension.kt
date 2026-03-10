package br.com.claus.sellvia.application.mapper

import br.com.claus.sellvia.application.dto.request.CategoryRequestDTO
import br.com.claus.sellvia.application.dto.response.CategoryResponseDTO
import br.com.claus.sellvia.domain.model.Category
import br.com.claus.sellvia.domain.model.Company

fun CategoryRequestDTO.toDomain(): Category =
    Category(
        id = this.id,
        name = this.name,
        description = this.description,
        company =
            Company(
                id = this.companyId
            ),
        createdAt = null,
        updatedAt = null,
        createdBy = null,
        updatedBy = null,
    )

fun Category.toResponseDTO(): CategoryResponseDTO =
    CategoryResponseDTO(
        id = this.id,
        name = this.name,
        description = this.description,
        companyId = this.company?.id ?: 0L,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        createdBy = this.createdBy,
        updatedBy = this.updatedBy
    )