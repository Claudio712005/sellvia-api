package br.com.claus.sellvia.domain.pagination

import br.com.claus.sellvia.domain.enums.Direction

open class SearchQuery(
    val page: Int = 0,
    val perPage: Int = 10,
    val terms: String = "",
    val sort: String = "id",
    val direction: Direction = Direction.ASC
) {

    init {
        require(page >= 0) { "Página inválida" }
        require(perPage in 1..100) { "Tamanho da página inválido" }
    }
}