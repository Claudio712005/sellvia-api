package br.com.claus.sellvia.application.usecase.category

import br.com.claus.sellvia.application.dto.response.CategoryResponseDTO
import br.com.claus.sellvia.application.mapper.toResponseDTO
import br.com.claus.sellvia.application.port.TokenServicePort
import br.com.claus.sellvia.application.service.PermissionServiceHelper
import br.com.claus.sellvia.domain.annotation.UseCase
import br.com.claus.sellvia.domain.enums.UserRole
import br.com.claus.sellvia.domain.exception.InvalidTokenException
import br.com.claus.sellvia.domain.exception.WithoutPermissionException
import br.com.claus.sellvia.domain.pagination.CategorySearchQuery
import br.com.claus.sellvia.domain.pagination.Pagination
import br.com.claus.sellvia.domain.repository.CategoryRepository

@UseCase
class FindPageableCategoryUseCase(
    private val categoryRepository: CategoryRepository,
    private val tokenServicePort: TokenServicePort,
    private val permissionServiceHelper: PermissionServiceHelper = PermissionServiceHelper(tokenServicePort)
) {

    fun execute(
        searchQuery: CategorySearchQuery
    ): Pagination<CategoryResponseDTO> {

        permissionServiceHelper.verifyUserCanDoesThisAction(searchQuery.companyId ?: 0)

        return categoryRepository.findBySearchQueryPageable(searchQuery)
            .map { it.toResponseDTO() }
    }
}