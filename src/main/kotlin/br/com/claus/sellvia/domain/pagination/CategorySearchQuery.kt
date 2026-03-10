package br.com.claus.sellvia.domain.pagination

import br.com.claus.sellvia.domain.enums.Direction

class CategorySearchQuery(
    page: Int,
    perPage: Int,
    terms: String,
    sort: String,
    direction: Direction,
    var companyId: Long? = null,
) : SearchQuery(
        page,
        perPage,
        terms,
        sort,
        direction
    )