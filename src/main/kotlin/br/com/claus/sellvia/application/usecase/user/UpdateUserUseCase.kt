package br.com.claus.sellvia.application.usecase.user

import br.com.claus.sellvia.application.dto.request.UserRequestDTO
import br.com.claus.sellvia.application.dto.response.UserResponseDTO
import br.com.claus.sellvia.application.mapper.toResponseDTO
import br.com.claus.sellvia.application.port.PermissionHelperPort
import br.com.claus.sellvia.domain.annotation.UseCase
import br.com.claus.sellvia.domain.repository.UserRepository

@UseCase
class UpdateUserUseCase(
    private val repository: UserRepository,
    private val permissionHelperPort: PermissionHelperPort,
) {
    fun execute(
        id: Long,
        user: UserRequestDTO,
    ): UserResponseDTO {
        val authenticatedUser = permissionHelperPort.getDetailsOfAuthenticatedUser()

        if (authenticatedUser.userId != id) {
            throw Exception("Usuário sem permissão para atualizar outro usuário.")
        }

        return repository.findById(id)?.let {
            repository.save(
                it.copy(
                    name = user.name ?: it.name,
                    email = user.email ?: it.email,
                    username = user.username ?: it.username,
                )
            ).toResponseDTO()
        } ?: throw Exception("Usuário com o id $id não encontrado.")
    }
}