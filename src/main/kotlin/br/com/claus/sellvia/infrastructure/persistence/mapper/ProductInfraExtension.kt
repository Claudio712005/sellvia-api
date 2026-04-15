package br.com.claus.sellvia.infrastructure.persistence.mapper

import br.com.claus.sellvia.domain.model.Product
import br.com.claus.sellvia.infrastructure.persistence.model.ProductEntity

fun Product.toEntity() =
    ProductEntity(
        id = id,
        sku = sku,
        name = name,
        price = price,
        productionCost = productionCost,
        stockQuantity = stockQuantity,
        type = type,
        company = company.toEntity(),
        description = description,
        status = status,
        imageUrl = imageUrl,
        createdAt = createdAt,
        updatedAt = updatedAt,
        createdBy = createdBy,
        updatedBy = updatedBy,
        category = category?.toEntity(),
        externalLink = externalLink,
        whatsappMessage = whatsappMessage,
    )

fun ProductEntity.toDomain() =
    Product(
        id = id!!,
        sku = sku,
        name = name,
        price = price,
        productionCost = productionCost,
        stockQuantity = stockQuantity,
        type = type,
        company = company.toDomain(),
        description = description,
        status = status,
        imageUrl = imageUrl,
        createdAt = createdAt,
        updatedAt = updatedAt,
        createdBy = createdBy,
        updatedBy = updatedBy,
        category = category?.toDomain(),
        externalLink = externalLink,
        whatsappMessage = whatsappMessage,
    )