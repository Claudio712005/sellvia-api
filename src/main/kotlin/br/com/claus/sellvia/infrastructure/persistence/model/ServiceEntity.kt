package br.com.claus.sellvia.infrastructure.persistence.model

import br.com.claus.sellvia.domain.enums.ResourceStatus
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "services")
@DiscriminatorValue("SERVICE")
class ServiceEntity(
    id: Long? = null,
    name: String,
    price: BigDecimal,
    description: String,
    productionCost: BigDecimal,
    company: CompanyEntity,
    status: ResourceStatus,
    imageUrl: String? = null,
    createdAt: LocalDateTime? = null,
    updatedAt: LocalDateTime? = null,
    createdBy: String? = null,
    updatedBy: String? = null,
    category: CategoryEntity? = null,
    val estimatedDurationInMinutes: Int,
) : SaleableItemEntity(
        id,
        name,
        description,
        price,
        productionCost,
        imageUrl,
        company,
        category,
        status,
        createdAt,
        updatedAt,
        createdBy,
        updatedBy
    )