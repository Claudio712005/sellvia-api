package br.com.claus.sellvia.application.usecase.auth

import br.com.claus.sellvia.application.dto.request.LoginRequestDTO
import br.com.claus.sellvia.application.dto.response.LoginResponseDTO
import br.com.claus.sellvia.application.port.PasswordEncoderPort
import br.com.claus.sellvia.application.port.TokenServicePort
import br.com.claus.sellvia.application.service.AuthServiceHelper
import br.com.claus.sellvia.domain.annotation.UseCase
import br.com.claus.sellvia.domain.exception.InvalidCredentialsException
import br.com.claus.sellvia.domain.repository.UserRepository

@UseCase
class LoginUseCase(
    val tokenServicePort: TokenServicePort,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoderPort,
    private val authServiceHelper: AuthServiceHelper = AuthServiceHelper(tokenService = tokenServicePort),
) {
    fun execute(request: LoginRequestDTO): LoginResponseDTO {
        request.validate()

        val user =
            userRepository.findByUsername(request.username!!)
                ?: throw InvalidCredentialsException("Usuário não encontrado ou credencias inválidas.")

        if (!passwordEncoder.matches(request.password!!, user.password)) {
            throw InvalidCredentialsException("Usuário não encontrado ou credencias inválida3.")
        }

        return authServiceHelper.createLoginResponse(user)
    }
}