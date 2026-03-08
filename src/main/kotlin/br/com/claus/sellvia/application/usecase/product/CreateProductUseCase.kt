package br.com.claus.sellvia.application.usecase.product

import br.com.claus.sellvia.application.dto.request.ProductRequestDTO
import br.com.claus.sellvia.application.mapper.toDomain
import br.com.claus.sellvia.application.port.PermissionHelperPort
import br.com.claus.sellvia.application.port.store.ProcessorResourceStorePort
import br.com.claus.sellvia.domain.annotation.UseCase
import br.com.claus.sellvia.domain.enums.FolderDestination
import br.com.claus.sellvia.domain.exception.NotFoundResouceException
import br.com.claus.sellvia.domain.exception.ResourceAlreadyExistsException
import br.com.claus.sellvia.domain.model.Category
import br.com.claus.sellvia.domain.repository.CategoryRepository
import br.com.claus.sellvia.domain.repository.ProductRepository

@UseCase
class CreateProductUseCase(
    private val repository: ProductRepository,
    private val permissionServicePort: PermissionHelperPort,
    private val processorResourceStorePort: ProcessorResourceStorePort,
    private val categoryRepository: CategoryRepository
) {

    fun execute(request: ProductRequestDTO, image: ByteArray) {

        val normalizedSku = request.sku.trim().uppercase()
        val normalizedName = request.name.trim()

        request.validate()

        validateBusinessRules(normalizedSku, normalizedName, request.companyId)

        request.categoryId?.let {
            if(!categoryRepository.existsByIdAndCompanyId(it, request.companyId)) {
                throw NotFoundResouceException("Categoria com id '$it' não encontrada para esta empresa.")
            }
        }

        val product = request.toDomain()

        val entity = repository.create(product)

        try {
            val key = processorResourceStorePort.saveOptimizedImage(
                image,
                request.companyId,
                FolderDestination.PRODUCT,
                entity.id!!
            )

            repository.update(
                entity.copy(
                    imageUrl = key
                )
            )
        } catch (e: Exception) {
            repository.delete(entity.id!!)
            throw e
        }
    }

    private fun validateBusinessRules(sku: String, name: String, companyId: Long) {
        permissionServicePort.verifyUserCanDoesThisAction(companyId)

        if (repository.existsBySkuAndCompanyId(sku, companyId)) {
            throw ResourceAlreadyExistsException("Produto com SKU '$sku' já existe para esta empresa.")
        }

        if (repository.existsByNameAndCompanyId(name, companyId)) {
            throw ResourceAlreadyExistsException("Produto com nome '$name' já existe para esta empresa.")
        }
    }
}