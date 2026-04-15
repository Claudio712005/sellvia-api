package br.com.claus.sellvia.domain.pagination

import br.com.claus.sellvia.domain.enums.Direction
import br.com.claus.sellvia.domain.enums.ProductType

data class CatalogFilterQuery(
    val sort: String = "name",
    val direction: Direction = Direction.ASC,
    val name: String? = null,
    val categoryId: Long? = null,
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val active: Boolean? = null,
    val sku: String? = null,
    val type: ProductType? = null,
)