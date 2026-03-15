package br.com.claus.sellvia.application.usecase.product

import br.com.claus.sellvia.application.dto.response.ProductResponseDTO
import br.com.claus.sellvia.application.mapper.toResponseDTO
import br.com.claus.sellvia.application.port.PermissionHelperPort
import br.com.claus.sellvia.application.port.store.SystemStoragePort
import br.com.claus.sellvia.domain.annotation.UseCase
import br.com.claus.sellvia.domain.enums.UserRole
import br.com.claus.sellvia.domain.pagination.Pagination
import br.com.claus.sellvia.domain.pagination.ProductSearchQuery
import br.com.claus.sellvia.domain.repository.ProductRepository

@UseCase
class FindPageableProductUseCase(
    private val repository: ProductRepository,
    private val permissionHelperPort: PermissionHelperPort,
    private val storagePort: SystemStoragePort,
) {
    fun execute(query: ProductSearchQuery): Pagination<ProductResponseDTO> {
        val authenticatedUser = permissionHelperPort.getDetailsOfAuthenticatedUser()

        val companyId =
            (query.companyId == null && UserRole.SYSTEM_ADMIN != authenticatedUser.role)
                .let { if (it) authenticatedUser.companyId else query.companyId }

        query.companyId = companyId

        return repository.findAll(
            query
        ).map {
            it.toResponseDTO().copy(
                imageUrl = storagePort.buildFileUrl(it.imageUrl)
            )
        }
    }
}