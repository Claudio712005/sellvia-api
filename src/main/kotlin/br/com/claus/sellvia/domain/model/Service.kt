package br.com.claus.sellvia.domain.model

import br.com.claus.sellvia.domain.enums.ResourceStatus
import java.math.BigDecimal
import java.time.LocalDateTime

data class Service(
    override val id: Long? = null,
    override val name: String,
    override val price: BigDecimal,
    override val productionCost: BigDecimal,
    val estimatedDurationInMinutes: Int,
    override val company: Company,
    override val description: String,
    override val status: ResourceStatus = ResourceStatus.ACTIVE,
    override val createdAt: LocalDateTime? = null,
    override val updatedAt: LocalDateTime? = null,
    override val createdBy: String? = null,
    override val updatedBy: String? = null,
    override val imageUrl: String? = null,
) : SaleableItem(id, name, description, price, productionCost, company, status, createdAt, updatedAt, createdBy, updatedBy, imageUrl) {
    override fun isAvailable(): Boolean = status == ResourceStatus.ACTIVE
}