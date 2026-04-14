package br.com.claus.sellvia.domain.model

import br.com.claus.sellvia.domain.enums.ProductType
import br.com.claus.sellvia.domain.enums.ResourceStatus
import java.math.BigDecimal
import java.time.LocalDateTime

data class Product(
    override val id: Long? = null,
    val sku: String,
    override val name: String,
    override val price: BigDecimal,
    override val productionCost: BigDecimal,
    val stockQuantity: Int?,
    val type: ProductType,
    override val company: Company,
    override val description: String,
    override val status: ResourceStatus = ResourceStatus.ACTIVE,
    override val createdAt: LocalDateTime? = null,
    override val updatedAt: LocalDateTime? = null,
    override val createdBy: String? = null,
    override val updatedBy: String? = null,
    override val imageUrl: String? = null,
    override val category: Category? = null,
    val externalLink: String? = null,
    val whatsappMessage: String? = null,
) : SaleableItem(
        id,
        name,
        description,
        price,
        productionCost,
        company,
        status,
        createdAt,
        updatedAt,
        createdBy,
        updatedBy,
        imageUrl,
        category
    ) {
    override fun isAvailable(): Boolean =
        if (type == ProductType.PHYSICAL) {
            status == ResourceStatus.ACTIVE && (stockQuantity != null && stockQuantity > 0)
        } else {
            status == ResourceStatus.ACTIVE
        }
}