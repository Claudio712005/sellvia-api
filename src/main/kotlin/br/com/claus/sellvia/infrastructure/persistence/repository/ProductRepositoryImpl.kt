package br.com.claus.sellvia.infrastructure.persistence.repository

import br.com.claus.sellvia.domain.model.Product
import br.com.claus.sellvia.domain.pagination.Pagination
import br.com.claus.sellvia.domain.pagination.ProductSearchQuery
import br.com.claus.sellvia.domain.repository.ProductRepository
import br.com.claus.sellvia.infrastructure.persistence.jpa.SpringDataProductRepository
import br.com.claus.sellvia.infrastructure.persistence.mapper.toDomain
import br.com.claus.sellvia.infrastructure.persistence.mapper.toEntity
import br.com.claus.sellvia.infrastructure.persistence.specification.ProductSpecificationFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrNull

@Component
class ProductRepositoryImpl(
    private val springDataRepository: SpringDataProductRepository,
) : ProductRepository {
    override fun create(product: Product): Product {
        return springDataRepository.save(product.copy(id = null).toEntity()).toDomain()
    }

    override fun findById(id: Long): Product? {
        return springDataRepository.findById(id).getOrNull()?.toDomain()
    }

    override fun existsById(id: Long): Boolean {
        return springDataRepository.existsById(id)
    }

    override fun existsBySkuAndCompanyId(
        sku: String,
        companyId: Long,
    ): Boolean {
        return springDataRepository.existsBySkuAndCompanyId(sku, companyId)
    }

    override fun existsBySkuAndCompanyIdAndNotId(
        sku: String,
        companyId: Long,
        id: Long,
    ): Boolean {
        return existsByNameAndCompanyIdAndNotId(sku, companyId, id)
    }

    override fun existsByNameAndCompanyId(
        name: String,
        companyId: Long,
    ): Boolean {
        return springDataRepository.existsByNameAndCompanyId(name, companyId)
    }

    override fun existsByNameAndCompanyIdAndNotId(
        name: String,
        companyId: Long,
        id: Long,
    ): Boolean {
        return springDataRepository.existsByNameAndCompanyIdAndIdNot(name, companyId, id)
    }

    override fun update(product: Product): Product {
        if (product.id == null) {
            throw IllegalArgumentException("Product ID cannot be null for update")
        }
        return springDataRepository.save(product.toEntity()).toDomain()
    }

    override fun delete(id: Long) {
        springDataRepository.deleteById(id)
    }

    override fun findBySkuAndCompanyId(
        sku: String,
        companyId: Long,
    ): Product? {
        return springDataRepository.findBySkuAndCompanyId(sku, companyId)?.toDomain()
    }

    override fun findAll(query: ProductSearchQuery): Pagination<Product> {
        val sort = Sort.by(Sort.Direction.fromString(query.direction.name), query.sort)

        val pageRequest = PageRequest.of(query.page, query.perPage, sort)

        val spec = ProductSpecificationFactory.build(query)

        val page = springDataRepository.findAll(spec, pageRequest)

        return Pagination(
            currentPage = page.number,
            perPage = page.size,
            totalItems = page.totalElements,
            items = page.content.map { it.toDomain() },
            totalPages = page.totalPages
        )
    }
}