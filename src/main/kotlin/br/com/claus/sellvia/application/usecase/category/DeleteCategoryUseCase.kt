package br.com.claus.sellvia.application.usecase.category

import br.com.claus.sellvia.application.port.TokenServicePort
import br.com.claus.sellvia.application.service.PermissionServiceHelper
import br.com.claus.sellvia.domain.annotation.UseCase
import br.com.claus.sellvia.domain.exception.NotFoundResouceException
import br.com.claus.sellvia.domain.repository.CategoryRepository

@UseCase
class DeleteCategoryUseCase(
    private val categoryRepository: CategoryRepository,
    private val tokenService: TokenServicePort,
    private val permissionServiceHelper: PermissionServiceHelper = PermissionServiceHelper(tokenService)
) {

    fun execute(id: Long) {
        if(id <= 0) {
            throw IllegalArgumentException("ID da categoria deve ser maior que zero")
        }

        val category = categoryRepository.findById(id)
            ?: throw NotFoundResouceException("Categoria com ID $id não encontrada")

        permissionServiceHelper.verifyUserCanDoesThisAction(category.company?.id ?: 0)

        categoryRepository.deleteById(id)
    }
}