package br.com.claus.sellvia.domain.pagination

import br.com.claus.sellvia.domain.enums.Direction

open class SearchQuery(
    open val page: Int = 0,
    open val perPage: Int = 10,
    open val terms: String = "",
    open val sort: String = "id",
    open val direction: Direction = Direction.ASC,
) {
    init {
        require(page >= 0) { "Página inválida" }
        require(perPage in 1..100) { "Tamanho da página inválido" }
    }
}