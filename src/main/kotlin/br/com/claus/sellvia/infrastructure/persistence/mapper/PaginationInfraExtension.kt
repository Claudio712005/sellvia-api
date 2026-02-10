package br.com.claus.sellvia.infrastructure.persistence.mapper

import br.com.claus.sellvia.domain.pagination.Pagination
import org.springframework.data.domain.Page

fun <T : Any> Page<T>.toDomainPagination(): Pagination<T> {
    return Pagination(
        items = this.content,
        currentPage = this.number,
        perPage = this.size,
        totalItems = this.totalElements,
        totalPages = this.totalPages
    )
}