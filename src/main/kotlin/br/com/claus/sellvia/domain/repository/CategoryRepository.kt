package br.com.claus.sellvia.domain.repository

import br.com.claus.sellvia.domain.model.Category
import br.com.claus.sellvia.domain.pagination.CategorySearchQuery
import br.com.claus.sellvia.domain.pagination.Pagination

interface CategoryRepository {

    fun findById(id: Long): Category?
    fun save(category: Category): Category
    fun deleteById(id: Long)
    fun findAll(): List<Category>
    fun findByNameAndCompanyId(name: String, companyId: Long): Category?
    fun findBySearchQueryPageable(searchQuery: CategorySearchQuery): Pagination<Category>
    fun existsByIdAndCompanyId(id: Long, companyId: Long): Boolean
}