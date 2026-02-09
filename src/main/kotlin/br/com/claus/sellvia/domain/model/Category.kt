package br.com.claus.sellvia.domain.model

import java.time.LocalDateTime

data class Category(
    val id : Long? = null,
    val name: String? = null,
    val description: String? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    val createdBy: String? =  null,
    val updatedBy: String? = null,

    val company: Company? = null,
)
