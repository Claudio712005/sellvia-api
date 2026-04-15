package br.com.claus.sellvia.application.usecase.product

import br.com.claus.sellvia.application.dto.response.ProductResponseDTO
import br.com.claus.sellvia.application.mapper.toResponseDTO
import br.com.claus.sellvia.application.port.PermissionHelperPort
import br.com.claus.sellvia.application.port.store.ProcessorResourceStorePort
import br.com.claus.sellvia.application.port.store.SystemStoragePort
import br.com.claus.sellvia.domain.annotation.UseCase
import br.com.claus.sellvia.domain.enums.FolderDestination
import br.com.claus.sellvia.domain.exception.NotFoundResouceException
import br.com.claus.sellvia.domain.repository.ProductRepository

@UseCase
class UpdateProductImageUseCase(
    private val repository: ProductRepository,
    private val permissionHelperPort: PermissionHelperPort,
    private val processorResourceStorePort: ProcessorResourceStorePort,
    private val systemStoragePort: SystemStoragePort,
) {
    fun execute(
        id: Long,
        image: ByteArray,
    ): ProductResponseDTO {
        val product =
            repository.findById(id)
                ?: throw NotFoundResouceException("Produto com ID $id não encontrado.")

        permissionHelperPort.verifyUserCanDoesThisAction(product.company.id)

        product.imageUrl?.takeIf { it.isNotBlank() }?.let { systemStoragePort.delete(it) }

        val key =
            processorResourceStorePort.saveOptimizedImage(
                image,
                product.company.id!!,
                FolderDestination.PRODUCT,
                id,
            )

        return repository.update(product.copy(imageUrl = key)).toResponseDTO()
    }
}