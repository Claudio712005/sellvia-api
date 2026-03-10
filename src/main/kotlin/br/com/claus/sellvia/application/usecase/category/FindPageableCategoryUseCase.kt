package br.com.claus.sellvia.application.usecase.category

import br.com.claus.sellvia.application.dto.response.CategoryResponseDTO
import br.com.claus.sellvia.application.mapper.toResponseDTO
import br.com.claus.sellvia.application.port.PermissionHelperPort
import br.com.claus.sellvia.domain.annotation.UseCase
import br.com.claus.sellvia.domain.enums.UserRole
import br.com.claus.sellvia.domain.pagination.CategorySearchQuery
import br.com.claus.sellvia.domain.pagination.Pagination
import br.com.claus.sellvia.domain.repository.CategoryRepository

@UseCase
class FindPageableCategoryUseCase(
    private val categoryRepository: CategoryRepository,
    private val permissionHelperPort: PermissionHelperPort,
) {
    fun execute(searchQuery: CategorySearchQuery): Pagination<CategoryResponseDTO> {
        val userDetails = permissionHelperPort.getDetailsOfAuthenticatedUser()

        val finalCompanyId =
            if (userDetails.role == UserRole.SYSTEM_ADMIN) {
                searchQuery.companyId
            } else {
                userDetails.companyId
            }

        permissionHelperPort.verifyUserCanDoesThisAction(finalCompanyId)

        searchQuery.companyId = finalCompanyId

        return categoryRepository.findBySearchQueryPageable(searchQuery)
            .map { it.toResponseDTO() }
    }
}