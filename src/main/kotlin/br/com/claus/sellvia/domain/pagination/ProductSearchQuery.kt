package br.com.claus.sellvia.domain.pagination

import br.com.claus.sellvia.domain.enums.Direction
import br.com.claus.sellvia.domain.enums.ProductType
import java.time.LocalDateTime

class ProductSearchQuery(
    perPage: Int = 10,
    page: Int = 0,
    sort: String = "id",
    direction: Direction = Direction.ASC,
    var companyId: Long? = null,
    val name: String? = null,
    val categoryId: Long? = null,
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val minCreatedAt: LocalDateTime? = null,
    val maxCreatedAt: LocalDateTime? = null,
    val minUpdatedAt: LocalDateTime? = null,
    val maxUpdatedAt: LocalDateTime? = null,
    val active: Boolean? = null,
    val createdBy: String? = null,
    val updatedBy: String? = null,
    val id: Long? = null,
    val sku: String? = null,
    val type: ProductType? = null,
) : SearchQuery(
        page,
        perPage,
        "",
        sort,
        direction
    )