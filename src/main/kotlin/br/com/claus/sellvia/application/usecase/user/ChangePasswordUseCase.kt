package br.com.claus.sellvia.application.usecase.user

import br.com.claus.sellvia.application.dto.request.UpdatePasswordRequestDTO
import br.com.claus.sellvia.application.port.PasswordEncoderPort
import br.com.claus.sellvia.application.port.PermissionHelperPort
import br.com.claus.sellvia.domain.annotation.UseCase
import br.com.claus.sellvia.domain.repository.UserRepository

@UseCase
class ChangePasswordUseCase(
    private val repository: UserRepository,
    private val passwordEncoderPort: PasswordEncoderPort,
    private val permissionHelperPort: PermissionHelperPort,
) {
    fun execute(
        id: Long,
        request: UpdatePasswordRequestDTO,
    ): String {
        val authenticatedUser = permissionHelperPort.getDetailsOfAuthenticatedUser()

        if (authenticatedUser.userId != id) {
            throw Exception("Usuário sem permissão para atualizar outro usuário.")
        }

        val user = repository.findById(id) ?: throw Exception("Usuário com o id $id não encontrado.")

        if (request.validate()) {
            if (!passwordEncoderPort.matches(request.password, user.password)) {
                throw Exception("Senha atual incorreta.")
            }

            val newEncodedPassword =
                passwordEncoderPort.encode(
                    request.newPassword,
                ) ?: throw Exception("Erro ao codificar a nova senha.")

            repository.save(user.copy(password = newEncodedPassword))
        } else {
            throw Exception("Dados de senha inválidos.")
        }

        return "Senha atualizada com sucesso."
    }
}