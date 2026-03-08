package br.com.claus.sellvia.application.usecase.product

import br.com.claus.sellvia.application.port.PermissionHelperPort
import br.com.claus.sellvia.domain.annotation.UseCase
import br.com.claus.sellvia.domain.repository.ProductRepository

@UseCase
class DeleteProductUseCase(
    private val repository: ProductRepository,
    private val permissionHelperPort: PermissionHelperPort,
) {

    fun execute(id: Long) {

        if (id <= 0) {
            throw IllegalArgumentException("ID do produto deve ser um número positivo.")
        }

        val product = repository.findById(id) ?: throw IllegalArgumentException("Produto com ID $id não encontrado.")

        permissionHelperPort.verifyUserCanDoesThisAction(product.company.id!!)

        repository.delete(id)
    }
}