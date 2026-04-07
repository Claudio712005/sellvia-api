package br.com.claus.sellvia.application.usecase.user

import br.com.claus.sellvia.application.dto.request.UserRequestDTO
import br.com.claus.sellvia.application.dto.response.UserResponseDTO
import br.com.claus.sellvia.application.mapper.toResponseDTO
import br.com.claus.sellvia.application.mapper.toUser
import br.com.claus.sellvia.application.port.PermissionHelperPort
import br.com.claus.sellvia.domain.annotation.UseCase
import br.com.claus.sellvia.domain.enums.UserRole
import br.com.claus.sellvia.domain.exception.ResourceAlreadyExistsException
import br.com.claus.sellvia.domain.exception.WithoutPermissionException
import br.com.claus.sellvia.domain.repository.UserRepository

@UseCase
class CreateUserUseCase(
    private val repository: UserRepository,
    private val permissionHelperPort: PermissionHelperPort,
) {
    fun execute(user: UserRequestDTO): UserResponseDTO {
        user.validate()

        val userdetails = permissionHelperPort.getDetailsOfAuthenticatedUser()

        if (userdetails.role == UserRole.COMPANY_USER) {
            throw WithoutPermissionException("Você não tem permissão para realizar essa ação.")
        } else if (userdetails.role == UserRole.COMPANY_ADMIN && userdetails.companyId != user.companyId) {
            throw WithoutPermissionException("Você não tem permissão para criar um usuário para essa empresa.")
        }

        if (repository.existsByEmail(user.email!!)) {
            throw ResourceAlreadyExistsException("Já existe um usuário com esse email.")
        }

        if (repository.existsByUsername(user.username!!)) {
            throw ResourceAlreadyExistsException("Já existe um usuário com esse username.")
        }

        if (repository.existsByCpf(user.cpf!!)) {
            throw ResourceAlreadyExistsException("Já existe um usuário com esse cpf.")
        }

        return repository.save(user.toUser()).toResponseDTO()
    }
}