package br.com.claus.sellvia.domain.model

import br.com.claus.sellvia.domain.enums.ResourceStatus
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime

abstract class SaleableItem(
    open val id: Long?,
    open val name: String,
    open val description: String,
    open val price: BigDecimal,
    open val productionCost: BigDecimal,
    open val company: Company,
    open val status: ResourceStatus,
    open val createdAt: LocalDateTime?,
    open val updatedAt: LocalDateTime?,
    open val createdBy: String?,
    open val updatedBy: String?,
    open val imageUrl: String? = null
) {
    fun profitMargin(): BigDecimal {
        if (productionCost <= BigDecimal.ZERO) return BigDecimal.ZERO
        return price.subtract(productionCost).divide(productionCost, 2, RoundingMode.HALF_UP)
    }

    abstract fun isAvailable(): Boolean
}