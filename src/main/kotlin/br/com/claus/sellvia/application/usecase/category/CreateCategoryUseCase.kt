package br.com.claus.sellvia.application.usecase.category

import br.com.claus.sellvia.application.dto.request.CategoryRequestDTO
import br.com.claus.sellvia.application.mapper.toDomain
import br.com.claus.sellvia.application.port.TokenServicePort
import br.com.claus.sellvia.application.service.PermissionServiceHelper
import br.com.claus.sellvia.domain.annotation.UseCase
import br.com.claus.sellvia.domain.exception.EntitiesConflictException
import br.com.claus.sellvia.domain.exception.WithoutPermissionException
import br.com.claus.sellvia.domain.repository.CategoryRepository

@UseCase
class CreateCategoryUseCase(
    private val repository: CategoryRepository,
    private val tokenService: TokenServicePort,
    private val permissionServiceHelper: PermissionServiceHelper = PermissionServiceHelper(tokenService)
) {

    fun execute(request: CategoryRequestDTO) {
        request.validate()

        if (repository.findByNameAndCompanyId(request.name!!, request.companyId!!) != null) {
            throw EntitiesConflictException("Já existe uma categoria com o nome '${request.name}' para esta empresa.")
        }

        permissionServiceHelper.verifyUserCanDoesThisAction(request.companyId)

        repository.save(request.toDomain())
    }
}