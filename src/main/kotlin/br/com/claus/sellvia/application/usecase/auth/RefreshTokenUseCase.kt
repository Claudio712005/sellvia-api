package br.com.claus.sellvia.application.usecase.auth

import br.com.claus.sellvia.application.dto.response.LoginResponseDTO
import br.com.claus.sellvia.application.port.TokenServicePort
import br.com.claus.sellvia.application.service.AuthServiceHelper
import br.com.claus.sellvia.domain.annotation.UseCase
import br.com.claus.sellvia.domain.exception.NotFoundResouceException
import br.com.claus.sellvia.domain.repository.UserRepository

@UseCase
class RefreshTokenUseCase(
    private val tokenService: TokenServicePort,
    private val userRepository: UserRepository,
    private val authServiceHelper: AuthServiceHelper = AuthServiceHelper(tokenService),
) {
    fun execute(refreshToken: String): LoginResponseDTO {
        val username =
            authServiceHelper
                .getUsernameByRefreshToken(refreshToken)

        val user =
            userRepository
                .findByUsername(username)
                ?: throw NotFoundResouceException("Usuário não encontrado")

        return authServiceHelper
            .createLoginResponse(user)
    }
}