package br.com.claus.sellvia.application.usecase.auth

import br.com.claus.sellvia.application.port.TokenServicePort
import br.com.claus.sellvia.application.port.store.SystemStoragePort
import br.com.claus.sellvia.application.service.AuthServiceHelper
import br.com.claus.sellvia.domain.enums.UserRole
import br.com.claus.sellvia.domain.exception.InvalidTokenException
import br.com.claus.sellvia.domain.exception.NotFoundResouceException
import br.com.claus.sellvia.domain.model.User
import br.com.claus.sellvia.domain.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime

class RefreshTokenUseCaseTest {
    private val userRepository = mockk<UserRepository>()
    private val tokenService = mockk<TokenServicePort>()
    private val systemStoragePort = mockk<SystemStoragePort>()

    private val refreshTokenUseCase =
        RefreshTokenUseCase(
            tokenService,
            userRepository,
            systemStoragePort,
            AuthServiceHelper(tokenService, systemStoragePort)
        )

    @Test
    fun `should return LoginResponseDTO when refresh token is valid`() {
        val oldRefreshToken = "old-refresh-token"
        val username = "claus.sellvia"
        val user =
            User(
                id = 1L,
                username = username,
                password = "hashed-password",
                name = "Claus",
                cpf = "",
                isActive = true,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                createdBy = "",
                updatedBy = "",
                role = UserRole.SYSTEM_ADMIN,
                company = null,
                email = "email@gmail.com",
            )

        every { tokenService.validateRefreshToken(oldRefreshToken) } returns username
        every { userRepository.findByUsername(username) } returns user
        every { tokenService.generateToken(user) } returns "new-access-token"
        every { tokenService.generateRefreshToken(user) } returns "new-refresh-token"

        val response = refreshTokenUseCase.execute(oldRefreshToken)

        assertNotNull(response)
        assertEquals("new-access-token", response.token)
        assertEquals("new-refresh-token", response.refreshToken)
        assertEquals(username, response.user.username)

        verify(exactly = 1) { tokenService.validateRefreshToken(oldRefreshToken) }
        verify(exactly = 1) { userRepository.findByUsername(username) }
    }

    @Test
    fun `should throw InvalidTokenException when token service returns null or empty`() {
        val invalidToken = "invalid-token"
        every { tokenService.validateRefreshToken(invalidToken) } returns null

        assertThrows<InvalidTokenException> {
            refreshTokenUseCase.execute(invalidToken)
        }

        verify(exactly = 0) { userRepository.findByUsername(any()) }
    }

    @Test
    fun `should throw NotFoundResouceException when user does not exist`() {
        val validToken = "valid-token"
        val username = "unknown-user"

        every { tokenService.validateRefreshToken(validToken) } returns username
        every { userRepository.findByUsername(username) } returns null

        val exception =
            assertThrows<NotFoundResouceException> {
                refreshTokenUseCase.execute(validToken)
            }

        assertEquals("Usuário não encontrado", exception.message)
    }
}