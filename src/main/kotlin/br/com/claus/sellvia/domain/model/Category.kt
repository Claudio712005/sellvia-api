package br.com.claus.sellvia.domain.model

import java.time.LocalDateTime

data class Category(
    val id : Long? = null,
    var name: String? = null,
    var description: String? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    val createdBy: String? =  null,
    val updatedBy: String? = null,

    var company: Company? = null,
)
