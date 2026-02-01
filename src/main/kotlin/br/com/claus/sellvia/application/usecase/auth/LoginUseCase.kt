package br.com.claus.sellvia.application.usecase.auth

import br.com.claus.sellvia.application.dto.request.LoginRequestDTO
import br.com.claus.sellvia.application.dto.response.LoginResponseDTO
import br.com.claus.sellvia.application.port.PasswordEncoderPort
import br.com.claus.sellvia.application.port.TokenServicePort
import br.com.claus.sellvia.domain.exception.NotFoundResouceException
import br.com.claus.sellvia.domain.repository.UserRepository
import br.com.claus.sellvia.infrastructure.persistence.mapper.toResponseDTO
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class LoginUseCase(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoderPort,
    private val tokenService: TokenServicePort
) {

    fun execute(request: LoginRequestDTO): LoginResponseDTO {
        val user = userRepository.findByUsername(request.username)
            ?: throw NotFoundResouceException("Usuário não encontrado")

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Senha inválida")
        }

        val token = tokenService.generateToken(user)
        val refreshToken = tokenService.generateRefreshToken(user)

        return LoginResponseDTO(
            token = token,
            refreshToken = refreshToken,
            user = user.toResponseDTO()
        ).also { dto ->
            dto.company = user.company?.toResponseDTO()
        }
    }
}