package br.com.claus.sellvia.application.usecase

import br.com.claus.sellvia.application.dto.request.LoginRequestDTO
import br.com.claus.sellvia.application.port.PasswordEncoderPort
import br.com.claus.sellvia.application.port.TokenServicePort
import br.com.claus.sellvia.application.usecase.auth.LoginUseCase
import br.com.claus.sellvia.domain.enums.UserRole
import br.com.claus.sellvia.domain.model.User
import br.com.claus.sellvia.domain.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.mockito.Mockito.verify
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime
import kotlin.test.Test

class LoginUseCaseTest {

    private val userRepository = mockk<UserRepository>()
    private val passwordEncoder = mockk<PasswordEncoderPort>()
    private val tokenService = mockk<TokenServicePort>()

    private val loginUseCase = LoginUseCase(userRepository, passwordEncoder, tokenService)

    @Test
    fun `should return tokens when credentials are valid`() {
        val rawPassword = "password123"
        val hashSimulado = "hash_encriptado_pelo_encoder"

        val user = User(
            username = "claus",
            password = hashSimulado,
            id = 1L,
            name = "clau",
            cpf = "123456789",
            isActive = true,
            role = UserRole.SYSTEM_ADMIN,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            createdBy = "system",
            updatedBy = "system"
        )

        every { userRepository.findByUsername("claus") } returns user

        every { passwordEncoder.matches(rawPassword, hashSimulado) } returns true

        every { tokenService.generateToken(user) } returns "access_token"
        every { tokenService.generateRefreshToken(user) } returns "refresh_token"

        val result = loginUseCase.execute(LoginRequestDTO("claus", rawPassword))

        result.token shouldBe "access_token"
        result.refreshToken shouldBe "refresh_token"

        verify(exactly = 1) { userRepository.findByUsername("claus") }
    }

    @Test
    fun `should throw exception when password is invalid`() {
        every { userRepository.findByUsername(any()) } returns mockk(relaxed = true)
        every { passwordEncoder.matches(any(), any()) } returns false

        shouldThrow<ResponseStatusException> {
            loginUseCase.execute(LoginRequestDTO("123", "123456"))
        }
    }
}
