package br.com.claus.sellvia.domain.pagination

data class Pagination<T>(
    val items: List<T>,
    val currentPage: Int,
    val perPage: Int,
    val totalItems: Long,
    val totalPages: Int
)