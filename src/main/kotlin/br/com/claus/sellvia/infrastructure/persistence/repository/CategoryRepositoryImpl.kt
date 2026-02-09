package br.com.claus.sellvia.infrastructure.persistence.repository

import br.com.claus.sellvia.domain.model.Category
import br.com.claus.sellvia.domain.repository.CategoryRepository
import br.com.claus.sellvia.infrastructure.persistence.jpa.SpringDataCategoryRepository
import br.com.claus.sellvia.infrastructure.persistence.mapper.toDomain
import br.com.claus.sellvia.infrastructure.persistence.mapper.toEntity
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrNull

@Component
class CategoryRepositoryImpl(
    private val springDataRepository: SpringDataCategoryRepository
) : CategoryRepository {
    override fun findById(id: Long): Category? {
        return springDataRepository
            .findById(id)
            .getOrNull()
            ?.toDomain()
    }

    override fun save(category: Category): Category {
        return springDataRepository
            .save(category.toEntity())
            .toDomain()
    }

    override fun deleteById(id: Long) {
        springDataRepository
            .deleteById(id)
    }

    override fun findAll(): List<Category> {
        return springDataRepository
            .findAll()
            .map { it.toDomain() }
    }

    override fun findByNameAndCompanyId(
        name: String,
        companyId: Long
    ): Category? = springDataRepository
        .findByNameAndCompanyId(name, companyId)
        ?.toDomain()
}