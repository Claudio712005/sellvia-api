package br.com.claus.sellvia.domain.repository

import br.com.claus.sellvia.domain.model.Company

interface CompanyRepository {
    fun findById(id: Long): Company?
}