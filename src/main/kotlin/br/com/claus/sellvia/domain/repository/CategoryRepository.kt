package br.com.claus.sellvia.domain.repository

import br.com.claus.sellvia.domain.model.Category

interface CategoryRepository {

    fun findById(id: Long): Category?
    fun save(category: Category): Category
    fun deleteById(id: Long)
    fun findAll(): List<Category>
    fun findByNameAndCompanyId(name: String, companyId: Long): Category?
}