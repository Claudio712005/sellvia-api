package br.com.claus.sellvia.infrastructure.persistence.jpa

import br.com.claus.sellvia.infrastructure.persistence.model.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface SpringDataProductRepository :
    JpaRepository<ProductEntity, Long>,
    JpaSpecificationExecutor<ProductEntity> {
    fun existsBySkuAndCompanyId(
        sku: String,
        companyId: Long,
    ): Boolean

    fun existsByNameAndCompanyId(
        name: String,
        companyId: Long,
    ): Boolean

    fun existsByNameAndCompanyIdAndIdNot(
        name: String,
        companyId: Long,
        id: Long,
    ): Boolean

    fun findBySkuAndCompanyId(
        sku: String,
        companyId: Long,
    ): ProductEntity?
}