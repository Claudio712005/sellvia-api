package br.com.claus.sellvia.application.dto.request

import br.com.claus.sellvia.domain.enums.ProductType
import br.com.claus.sellvia.domain.enums.ResourceStatus
import java.math.BigDecimal

data class ProductRequestDTO(
        override val id: Long? = null,
        override val name: String,
        override val description: String,
        override val price: BigDecimal,
        override val productionCost: BigDecimal,
        override val companyId: Long,
        override val status: ResourceStatus = ResourceStatus.ACTIVE,
        override var imageUrl: String? = null,

        val sku: String,
        val stockQuantity: Int? = null,
        val type: ProductType = ProductType.PHYSICAL

    ) : SaleableItemRequestDTO(
    id = id,
    name = name,
    description = description,
    price = price,
    productionCost = productionCost,
    companyId = companyId,
    status = status,
    imageUrl = imageUrl
) {

    override fun validate() {
        super.validate()

        require(sku.isNotBlank()) { "SKU não pode ser vazio." }
        require(stockQuantity == null || stockQuantity >= 0) { "Quantidade deve ser maior ou igual a zero." }
    }
}
