package br.com.claus.sellvia.infrastructure.persistence.model

import br.com.claus.sellvia.domain.enums.ProductType
import br.com.claus.sellvia.domain.enums.ResourceStatus
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "products")
@DiscriminatorValue("PRODUCT")
class ProductEntity(
    id: Long? = null,
    name: String,
    description: String,
    price: BigDecimal,
    productionCost: BigDecimal,
    company: CompanyEntity,
    status: ResourceStatus,
    imageUrl: String? = null,
    createdBy: String? = null,
    updatedBy: String? = null,
    createdAt: LocalDateTime? = null,
    updatedAt: LocalDateTime? = null,

    val sku: String,
    val stockQuantity: Int? = null,

    @Enumerated(EnumType.STRING)
    val type: ProductType
) : SaleableItemEntity(id, name, description, price, productionCost, imageUrl, company, status, createdAt, updatedAt, createdBy, updatedBy)