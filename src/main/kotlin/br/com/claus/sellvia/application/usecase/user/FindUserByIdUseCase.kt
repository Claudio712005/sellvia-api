package br.com.claus.sellvia.application.usecase.user

import br.com.claus.sellvia.application.dto.response.UserResponseDTO
import br.com.claus.sellvia.application.mapper.toResponseDTO
import br.com.claus.sellvia.application.port.PermissionHelperPort
import br.com.claus.sellvia.domain.annotation.UseCase
import br.com.claus.sellvia.domain.enums.UserRole
import br.com.claus.sellvia.domain.exception.NotFoundResouceException
import br.com.claus.sellvia.domain.exception.WithoutPermissionException
import br.com.claus.sellvia.domain.model.User
import br.com.claus.sellvia.domain.repository.UserRepository

@UseCase
class FindUserByIdUseCase(
    private val repository: UserRepository,
    private val permissionHelperPort: PermissionHelperPort
) {

    fun execute(id: Long): UserResponseDTO{
        val authenticatedUser = permissionHelperPort.getDetailsOfAuthenticatedUser()

        if(authenticatedUser.role != UserRole.SYSTEM_ADMIN || authenticatedUser.userId != id){
            throw WithoutPermissionException("Usuário sem permissão para consultar outro usuário.")
        }

        return repository.findById(id)?.toResponseDTO() ?: throw NotFoundResouceException("Usuário com o id $id não encontrado.")
    }
}