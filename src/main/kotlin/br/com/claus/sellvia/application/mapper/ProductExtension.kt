package br.com.claus.sellvia.application.mapper

import br.com.claus.sellvia.application.dto.request.ProductRequestDTO
import br.com.claus.sellvia.application.dto.response.ProductResponseDTO
import br.com.claus.sellvia.domain.model.Category
import br.com.claus.sellvia.domain.model.Company
import br.com.claus.sellvia.domain.model.Product

fun ProductRequestDTO.toDomain() =
    Product(
        id = id,
        sku = sku,
        name = name,
        price = price,
        productionCost = productionCost,
        stockQuantity = stockQuantity,
        type = type,
        company = Company(id = companyId),
        description = description,
        status = status,
        createdAt = null,
        updatedAt = null,
        imageUrl = imageUrl,
        category = categoryId?.let { Category(id = it) }
    )

fun Product.toResponseDTO() =
    ProductResponseDTO(
        id = id!!,
        sku = sku,
        name = name,
        price = price,
        productionCost = productionCost,
        stockQuantity = stockQuantity,
        type = type,
        companyId = company.id!!,
        description = description,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt,
        createdBy = createdBy,
        updatedBy = updatedBy,
        imageUrl = imageUrl,
        category = category?.toResponseDTO()
    )