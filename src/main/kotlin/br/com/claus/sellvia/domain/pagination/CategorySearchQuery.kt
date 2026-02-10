package br.com.claus.sellvia.domain.pagination

import br.com.claus.sellvia.domain.enums.Direction

class CategorySearchQuery(
    page: Int,
    perPage: Int,
    terms: String,
    sort: String,
    direction: Direction,
    val companyId: Long? = null
): SearchQuery(
    page,
    perPage,
    terms,
    sort,
    direction)
