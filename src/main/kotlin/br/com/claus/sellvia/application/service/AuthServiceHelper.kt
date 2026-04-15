package br.com.claus.sellvia.application.service

import br.com.claus.sellvia.application.dto.response.LoginResponseDTO
import br.com.claus.sellvia.application.mapper.toResponseDTO
import br.com.claus.sellvia.application.port.TokenServicePort
import br.com.claus.sellvia.application.port.store.SystemStoragePort
import br.com.claus.sellvia.domain.exception.InvalidTokenException
import br.com.claus.sellvia.domain.model.User

class AuthServiceHelper(private val tokenService: TokenServicePort, private val systemStoragePort: SystemStoragePort) {
    fun createLoginResponse(user: User): LoginResponseDTO {
        val token =
            tokenService
                .generateToken(user)
        val refreshToken =
            tokenService
                .generateRefreshToken(user)

        return LoginResponseDTO(
            token = token,
            refreshToken = refreshToken,
            user = user.toResponseDTO()
        ).also { dto ->
            dto.company =
                user.company?.toResponseDTO()?.copy(
                    companyUrlLogo = systemStoragePort.buildFileUrl(user.company?.companyUrlLogo)
                )
        }
    }

    fun getUsernameByToken(token: String): String {
        return tokenService.validateToken(token)
            ?: throw InvalidTokenException("Token iválido")
    }

    fun getUsernameByRefreshToken(token: String): String {
        return tokenService.validateRefreshToken(token)
            ?: throw InvalidTokenException("Token iválido")
    }
}