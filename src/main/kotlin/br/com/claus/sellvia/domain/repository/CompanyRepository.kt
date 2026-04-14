package br.com.claus.sellvia.domain.repository

import br.com.claus.sellvia.domain.model.Company

interface CompanyRepository {
    fun findById(id: Long): Company?

    fun update(company: Company): Company

    fun existsByCnpjAndIdNot(
        cnpj: String,
        id: Long,
    ): Boolean

    fun existsByMainPhoneNumberAndIdNot(
        mainPhone: String,
        id: Long,
    ): Boolean
}