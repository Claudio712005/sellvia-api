package br.com.claus.sellvia.application.usecase.product

import br.com.claus.sellvia.application.dto.request.ProductRequestDTO
import br.com.claus.sellvia.application.dto.request.ProductRequestDTO.Companion.DEFAULT_WHATSAPP_MESSAGE
import br.com.claus.sellvia.application.dto.response.ProductResponseDTO
import br.com.claus.sellvia.application.mapper.toResponseDTO
import br.com.claus.sellvia.application.port.PermissionHelperPort
import br.com.claus.sellvia.domain.annotation.UseCase
import br.com.claus.sellvia.domain.exception.NotFoundResouceException
import br.com.claus.sellvia.domain.exception.ResourceAlreadyExistsException
import br.com.claus.sellvia.domain.repository.ProductRepository

@UseCase
class UpdateProductUseCase(
    private val repository: ProductRepository,
    private val permissionServicePort: PermissionHelperPort,
) {
    fun execute(
        requestDTO: ProductRequestDTO,
        id: Long,
    ): ProductResponseDTO {
        if (id <= 0) {
            throw IllegalArgumentException("ID do produto deve ser um número positivo.")
        }

        requestDTO.validate()

        validateBusinessRules(requestDTO.sku, requestDTO.name, requestDTO.companyId, id)

        val product = repository.findById(id) ?: throw NotFoundResouceException("Produto com ID $id não encontrado.")

        val updatedProduct =
            product.copy(
                name = requestDTO.name.trim(),
                description = requestDTO.description.trim(),
                price = requestDTO.price,
                productionCost = requestDTO.productionCost,
                status = requestDTO.status,
                sku = requestDTO.sku.trim().uppercase(),
                stockQuantity = requestDTO.stockQuantity,
                type = requestDTO.type,
                externalLink = requestDTO.externalLink?.trim()?.ifBlank { null },
                whatsappMessage =
                    requestDTO.whatsappMessage?.trim()?.takeIf { it.isNotBlank() }
                        ?: DEFAULT_WHATSAPP_MESSAGE,
            )

        return repository.update(updatedProduct).toResponseDTO()
    }

    private fun validateBusinessRules(
        sku: String,
        name: String,
        companyId: Long,
        id: Long,
    ) {
        permissionServicePort.verifyUserCanDoesThisAction(companyId)

        if (repository.existsBySkuAndCompanyIdAndNotId(sku, companyId, id)) {
            throw ResourceAlreadyExistsException("Produto com SKU '$sku' já existe para esta empresa.")
        }

        if (repository.existsByNameAndCompanyIdAndNotId(name, companyId, id)) {
            throw ResourceAlreadyExistsException("Produto com nome '$name' já existe para esta empresa.")
        }
    }
}