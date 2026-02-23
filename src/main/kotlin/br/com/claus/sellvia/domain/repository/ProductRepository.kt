package br.com.claus.sellvia.domain.repository

import br.com.claus.sellvia.domain.model.Product

interface ProductRepository {

    fun create(product: Product): Product
    fun findById(id: Long): Product?

    fun existsById(id: Long): Boolean
    fun existsBySkuAndCompanyId(sku: String, companyId: Long): Boolean
    fun existsBySkuAndCompanyIdAndNotId(sku: String, companyId: Long, id: Long): Boolean
    fun existsByNameAndCompanyId(name: String, companyId: Long): Boolean
    fun existsByNameAndCompanyIdAndNotId(name: String, companyId: Long, id: Long): Boolean

    fun update(product: Product): Product
    fun delete(id: Long)
    fun findBySkuAndCompanyId(sku: String, companyId: Long): Product?
}