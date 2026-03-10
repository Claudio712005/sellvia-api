package br.com.claus.sellvia.infrastructure.persistence.specification

import br.com.claus.sellvia.domain.enums.ResourceStatus
import br.com.claus.sellvia.domain.pagination.ProductSearchQuery
import br.com.claus.sellvia.infrastructure.persistence.model.ProductEntity
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification
import java.math.BigDecimal

object ProductSpecificationFactory {
    fun build(query: ProductSearchQuery): Specification<ProductEntity> {
        return Specification { root, _, cb ->
            val predicates = mutableListOf<Predicate>()

            query.id?.let { predicates.add(cb.equal(root.get<Long>("id"), it)) }

            query.companyId?.let {
                predicates.add(cb.equal(root.get<Any>("company").get<Long>("id"), it))
            }

            query.name?.takeIf { it.isNotBlank() }?.let {
                predicates.add(cb.like(cb.lower(root.get("name")), "%${it.lowercase()}%"))
            }

            query.active?.let { isActive ->
                val status = if (isActive) ResourceStatus.ACTIVE else ResourceStatus.INACTIVE
                predicates.add(cb.equal(root.get<ResourceStatus>("status"), status))
            }

            query.minPrice?.let {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), BigDecimal.valueOf(it)))
            }
            query.maxPrice?.let {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), BigDecimal.valueOf(it)))
            }

            query.minCreatedAt?.let { predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), it)) }
            query.maxCreatedAt?.let { predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), it)) }
            query.minUpdatedAt?.let { predicates.add(cb.greaterThanOrEqualTo(root.get("updatedAt"), it)) }
            query.maxUpdatedAt?.let { predicates.add(cb.lessThanOrEqualTo(root.get("updatedAt"), it)) }

            query.categoryId?.let { predicates.add(cb.equal(root.get<Any>("category").get<Any>("id"), it)) }
            query.sku?.let { predicates.add(cb.equal(root.get<Any>("sku"), it)) }

            cb.and(*predicates.toTypedArray())
        }
    }
}