package br.com.claus.sellvia.infrastructure.persistence.jpa

import br.com.claus.sellvia.infrastructure.persistence.model.CategoryEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SpringDataCategoryRepository: JpaRepository<CategoryEntity, Long> {

    fun findByNameAndCompanyId(name: String, companyId: Long): CategoryEntity?
}