package br.com.claus.sellvia.application.usecase.auth

import br.com.claus.sellvia.application.dto.request.UserRequestDTO
import br.com.claus.sellvia.application.mapper.toUser
import br.com.claus.sellvia.application.port.PasswordEncoderPort
import br.com.claus.sellvia.application.port.TokenServicePort
import br.com.claus.sellvia.application.service.AuthServiceHelper
import br.com.claus.sellvia.domain.annotation.UseCase
import br.com.claus.sellvia.domain.enums.UserRole
import br.com.claus.sellvia.domain.exception.NotFoundResouceException
import br.com.claus.sellvia.domain.exception.ResourceAlreadyExistsException
import br.com.claus.sellvia.domain.exception.WithoutPermissionException
import br.com.claus.sellvia.domain.model.User
import br.com.claus.sellvia.domain.repository.CompanyRepository
import br.com.claus.sellvia.domain.repository.UserRepository

@UseCase
class RegistryUserUseCase(
    private val userRepository: UserRepository,
    private val tokenService: TokenServicePort,
    private val companyRepository: CompanyRepository,
    private val passwordEncoder: PasswordEncoderPort,
    private val authServiceHelper: AuthServiceHelper = AuthServiceHelper(tokenService),
) {
    fun execute(
        requestDTO: UserRequestDTO,
        token: String,
    ) {
        requestDTO.validate()

        val userInToken = validatePermission(token)

        checkUserPersistenceConstraints(requestDTO)

        if (userInToken.role != UserRole.SYSTEM_ADMIN &&
            userInToken.company?.id != requestDTO.companyId
        ) {
            throw WithoutPermissionException("Você não pode cadastrar um usuário para uma empresa diferente da sua.")
        }

        val company =
            companyRepository
                .findById(
                    requestDTO.companyId
                        ?: throw NotFoundResouceException("A empresa para o usuário não pode ser encontrada")
                )
                ?: throw NotFoundResouceException("A empresa para o usuário não pode ser encontrada")

        val encodedPassword = passwordEncoder.encode(requestDTO.password!!)

        val user =
            requestDTO.toUser().copy(
                company = company,
                password = encodedPassword!!
            )

        userRepository.save(user)
    }

    private fun validatePermission(token: String): User {
        val username = authServiceHelper.getUsernameByToken(token)
        val userInToken =
            userRepository.findByUsername(username)
                ?: throw NotFoundResouceException("Usuário do token não encontrado.")

        if (userInToken.role == UserRole.COMPANY_USER) {
            throw WithoutPermissionException("Sem permissão para realizar essa ação.")
        }

        return userInToken
    }

    private fun checkUserPersistenceConstraints(dto: UserRequestDTO) {
        when {
            userRepository.existsByEmail(dto.email!!) ->
                throw ResourceAlreadyExistsException("E-mail já cadastrado no sistema.")

            userRepository.existsByUsername(dto.username!!) ->
                throw ResourceAlreadyExistsException("Nome de usuário já está em uso.")

            userRepository.existsByCpf(dto.cpf!!) ->
                throw ResourceAlreadyExistsException("CPF já cadastrado.")
        }
    }
}