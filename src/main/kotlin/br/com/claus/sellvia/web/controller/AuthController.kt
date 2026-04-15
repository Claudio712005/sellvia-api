package br.com.claus.sellvia.web.controller

import br.com.claus.sellvia.application.dto.request.LoginRequestDTO
import br.com.claus.sellvia.application.dto.request.UserRequestDTO
import br.com.claus.sellvia.application.dto.response.LoginResponseDTO
import br.com.claus.sellvia.application.usecase.auth.LoginUseCase
import br.com.claus.sellvia.application.usecase.auth.RefreshTokenUseCase
import br.com.claus.sellvia.application.usecase.auth.RegistryUserUseCase
import br.com.claus.sellvia.domain.exception.InvalidTokenException
import br.com.claus.sellvia.infrastructure.config.ApiEndpoints
import jakarta.validation.Valid
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(ApiEndpoints.Auth.AUTH_ROOT)
class AuthController(
    private val loginUseCase: LoginUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val registryUserUseCase: RegistryUserUseCase,
) {
    @PostMapping(ApiEndpoints.Auth.LOGIN)
    fun login(
        @Valid @RequestBody request: LoginRequestDTO,
    ): ResponseEntity<LoginResponseDTO> {
        val response = loginUseCase.execute(request)
        return ResponseEntity.ok(response)
    }

    @PostMapping(ApiEndpoints.Auth.REFRESH_TOKEN)
    fun refreshToken(
        @RequestHeader(HttpHeaders.AUTHORIZATION) authHeader: String?,
    ): ResponseEntity<LoginResponseDTO> {
        val response = refreshTokenUseCase.execute(getToken(authHeader))
        return ResponseEntity.ok(response)
    }

    @PostMapping(ApiEndpoints.Auth.REGISTRY)
    fun registryUser(
        @Valid @RequestBody request: UserRequestDTO,
        @RequestHeader(HttpHeaders.AUTHORIZATION) authHeader: String?,
    ): ResponseEntity<Any> {
        registryUserUseCase.execute(request, getToken(authHeader))
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    private fun getToken(authHeader: String?): String {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw InvalidTokenException("Header de autorização ausente ou inválido")
        }

        return authHeader.substring(7)
    }
}