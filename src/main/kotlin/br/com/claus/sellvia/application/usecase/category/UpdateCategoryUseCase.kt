package br.com.claus.sellvia.application.usecase.category

import br.com.claus.sellvia.application.dto.request.CategoryRequestDTO
import br.com.claus.sellvia.application.dto.response.CategoryResponseDTO
import br.com.claus.sellvia.application.mapper.toResponseDTO
import br.com.claus.sellvia.application.port.TokenServicePort
import br.com.claus.sellvia.application.service.PermissionServiceHelper
import br.com.claus.sellvia.domain.annotation.UseCase
import br.com.claus.sellvia.domain.enums.UserRole
import br.com.claus.sellvia.domain.exception.InvalidTokenException
import br.com.claus.sellvia.domain.exception.NotFoundResouceException
import br.com.claus.sellvia.domain.repository.CategoryRepository

@UseCase
class UpdateCategoryUseCase(
    private val repository: CategoryRepository,
    private val tokenService: TokenServicePort,
    private val permissionServiceHelper: PermissionServiceHelper = PermissionServiceHelper(tokenService)
) {

    fun execute(id: Long, request: CategoryRequestDTO): CategoryResponseDTO {
        request.validate()

        val category = repository.findById(id)
            ?: throw NotFoundResouceException("Categoria com ID $id não encontrada")

        permissionServiceHelper
            .verifyUserCanDoesThisAction(category.company?.id ?: 0)

        val categoryDuplicated = repository.findByNameAndCompanyId(
            name = request.name!!,
            companyId = category.company?.id ?: 0
        )

        if(categoryDuplicated != null && categoryDuplicated.id != category.id) {
            throw IllegalArgumentException("Já existe uma categoria com o nome '${request.name}' para esta empresa")
        }

        category.name = request.name!!
        category.description = request.description!!

        return repository.save(category).toResponseDTO()
    }
}