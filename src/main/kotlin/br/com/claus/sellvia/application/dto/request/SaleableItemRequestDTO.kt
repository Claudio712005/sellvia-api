package br.com.claus.sellvia.application.dto.request

import br.com.claus.sellvia.domain.enums.ResourceStatus
import java.math.BigDecimal

abstract class SaleableItemRequestDTO(
    open val id: Long?,
    open val name: String,
    open val description: String,
    open val price: BigDecimal,
    open val productionCost: BigDecimal,
    open val companyId: Long,
    open val status: ResourceStatus = ResourceStatus.ACTIVE,
    open var imageUrl: String? = null,
    open val categoryId: Long? = null,
) {
    open fun validate() {
        require(!name.isNullOrBlank()) { "Nome não pode ser vazio." }
        require(price >= BigDecimal.ZERO) { "Preço deve ser maior ou igual a zero." }
        require(productionCost >= BigDecimal.ZERO) { "Custo de produção deve ser maior ou igual a zero." }
        require(companyId != null) { "ID da empresa é obrigatório." }
        require(description.isNotBlank()) { "Descrição não pode ser vazia." }
        require(categoryId == null || categoryId!! > 0) { "ID da categoria deve ser maior que zero." }
    }
}