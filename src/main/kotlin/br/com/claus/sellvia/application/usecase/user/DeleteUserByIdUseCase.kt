package br.com.claus.sellvia.application.usecase.user

import br.com.claus.sellvia.application.port.PermissionHelperPort
import br.com.claus.sellvia.domain.annotation.UseCase
import br.com.claus.sellvia.domain.exception.NotFoundResouceException
import br.com.claus.sellvia.domain.exception.WithoutPermissionException
import br.com.claus.sellvia.domain.repository.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable

@UseCase
class DeleteUserByIdUseCase(
    private val repository: UserRepository,
    private val permissionHelperPort: PermissionHelperPort
) {

    fun execute(@PathVariable id: Long) {

        val userTokenId = permissionHelperPort.getDetailsOfAuthenticatedUser().userId

        if(id != userTokenId){
            throw WithoutPermissionException("Você não tem permissão para excluir este usuário.")
        }

        val user = repository.findById(id) ?: throw NotFoundResouceException("O usuário não foi encontrado.")

        repository.save(user.copy(isActive = false))
    }
}