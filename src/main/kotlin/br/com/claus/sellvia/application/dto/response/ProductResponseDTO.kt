package br.com.claus.sellvia.application.dto.response

import br.com.claus.sellvia.domain.enums.ProductType
import br.com.claus.sellvia.domain.enums.ResourceStatus
import java.math.BigDecimal
import java.time.LocalDateTime

data class ProductResponseDTO(
    override val id: Long,
    override val name: String,
    override val description: String,
    override val price: BigDecimal,
    override val productionCost: BigDecimal,
    override val companyId: Long,
    override val status: ResourceStatus,
    override val imageUrl: String? = null,
    override val createdAt: LocalDateTime? = null,
    override val updatedAt: LocalDateTime? = null,
    override val createdBy: String? = null,
    override val updatedBy: String? = null,
    override val category: CategoryResponseDTO? = null,
    val sku: String,
    val stockQuantity: Int? = null,
    val type: ProductType,
    val externalLink: String? = null,
    val whatsappMessage: String? = null,
) : SaleableItemResponseDTO(
        id,
        name,
        description,
        price,
        productionCost,
        companyId,
        status,
        imageUrl,
        category,
        createdAt,
        updatedAt,
        createdBy,
        updatedBy
    )