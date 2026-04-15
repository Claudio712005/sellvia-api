package br.com.claus.sellvia.domain.pagination

data class Pagination<T>(
    val items: List<T>,
    val currentPage: Int,
    val perPage: Int,
    val totalItems: Long,
    val totalPages: Int,
) {
    fun <R> map(mapper: (T) -> R): Pagination<R> {
        return Pagination(
            items = this.items.map(mapper),
            currentPage = this.currentPage,
            perPage = this.perPage,
            totalItems = this.totalItems,
            totalPages = this.totalPages
        )
    }
}