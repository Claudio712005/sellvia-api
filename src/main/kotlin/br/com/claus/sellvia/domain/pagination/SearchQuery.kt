package br.com.claus.sellvia.domain.pagination

open class SearchQuery(
    val page: Int,
    val perPage: Int,
    val terms: String,
    val sort: String,
    val direction: String,
)
