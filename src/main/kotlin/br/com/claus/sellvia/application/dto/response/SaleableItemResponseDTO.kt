package br.com.claus.sellvia.application.dto.response

import br.com.claus.sellvia.domain.enums.ResourceStatus
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import java.math.BigDecimal
import java.time.LocalDateTime

abstract class SaleableItemResponseDTO(
    open val id: Long?,
    open val name: String,
    open val description: String,
    open val price: BigDecimal,
    open val productionCost: BigDecimal,
    open val companyId: Long,
    open val status: ResourceStatus = ResourceStatus.ACTIVE,
    open val imageUrl: String?,

    open val createdAt: LocalDateTime? = null,
    open val updatedAt: LocalDateTime? = null,
    open val createdBy: String? =  null,
    open val updatedBy: String? = null,)
