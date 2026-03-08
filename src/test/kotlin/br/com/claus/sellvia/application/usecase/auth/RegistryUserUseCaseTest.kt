package br.com.claus.sellvia.application.usecase.auth

import br.com.claus.sellvia.application.dto.request.UserRequestDTO
import br.com.claus.sellvia.application.port.PasswordEncoderPort
import br.com.claus.sellvia.application.port.TokenServicePort
import br.com.claus.sellvia.application.service.AuthServiceHelper
import br.com.claus.sellvia.domain.enums.UserRole
import br.com.claus.sellvia.domain.exception.NotFoundResouceException
import br.com.claus.sellvia.domain.exception.ResourceAlreadyExistsException
import br.com.claus.sellvia.domain.exception.WithoutPermissionException
import br.com.claus.sellvia.domain.model.Company
import br.com.claus.sellvia.domain.model.User
import br.com.claus.sellvia.domain.repository.CompanyRepository
import br.com.claus.sellvia.domain.repository.UserRepository
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class RegistryUserUseCaseTest {
    private val userRepository = mockk<UserRepository>()
    private val tokenService = mockk<TokenServicePort>()
    private val companyRepository = mockk<CompanyRepository>()
    private val passwordEncoder = mockk<PasswordEncoderPort>()
    private val authServiceHelper = mockk<AuthServiceHelper>()

    private lateinit var useCase: RegistryUserUseCase

    @BeforeEach
    fun setup() {
        useCase =
            RegistryUserUseCase(
                userRepository,
                tokenService,
                companyRepository,
                passwordEncoder,
                authServiceHelper
            )
    }

    @Test
    fun `should register user successfully when data is valid`() {
        val token = "valid-token"
        val request = createValidRequest()
        val userInToken = createUser(role = UserRole.SYSTEM_ADMIN)
        val company = Company(id = 1L, name = "Test Company")

        every { authServiceHelper.getUsernameByToken(token) } returns "admin"
        every { userRepository.findByUsername("admin") } returns userInToken
        every { userRepository.existsByEmail(any()) } returns false
        every { userRepository.existsByUsername(any()) } returns false
        every { userRepository.existsByCpf(any()) } returns false
        every { companyRepository.findById(1L) } returns company
        every { passwordEncoder.encode(any()) } returns "encodedPassword"
        every { userRepository.save(any()) } returns mockk()

        useCase.execute(request, token)

        verify(exactly = 1) { userRepository.save(any()) }
    }

    @Test
    fun `should throw WithoutPermissionException when COMPANY_ADMIN registers user for different company`() {
        val token = "token"
        val request = createValidRequest().copy(companyId = 2L)
        val userInToken = createUser(role = UserRole.COMPANY_ADMIN, companyId = 1L)

        every { authServiceHelper.getUsernameByToken(token) } returns "manager"
        every { userRepository.findByUsername("manager") } returns userInToken
        every { userRepository.existsByEmail(any()) } returns false
        every { userRepository.existsByUsername(any()) } returns false
        every { userRepository.existsByCpf(any()) } returns false

        val exception =
            assertThrows<WithoutPermissionException> {
                useCase.execute(request, token)
            }
        assertEquals("Você não pode cadastrar um usuário para uma empresa diferente da sua.", exception.message)
    }

    @Test
    fun `should throw ResourceAlreadyExistsException when email already exists`() {
        val token = "token"
        val request = createValidRequest()
        val userInToken = createUser(role = UserRole.SYSTEM_ADMIN)

        every { authServiceHelper.getUsernameByToken(token) } returns "admin"
        every { userRepository.findByUsername("admin") } returns userInToken
        every { userRepository.existsByEmail(request.email!!) } returns true

        assertThrows<ResourceAlreadyExistsException> {
            useCase.execute(request, token)
        }
    }

    @Test
    fun `should throw NotFoundResouceException when company is not found`() {
        val token = "token"
        val request = createValidRequest()
        val userInToken = createUser(role = UserRole.SYSTEM_ADMIN)

        every { authServiceHelper.getUsernameByToken(token) } returns "admin"
        every { userRepository.findByUsername("admin") } returns userInToken
        every { userRepository.existsByEmail(any()) } returns false
        every { userRepository.existsByUsername(any()) } returns false
        every { userRepository.existsByCpf(any()) } returns false
        every { companyRepository.findById(request.companyId!!) } returns null

        assertThrows<NotFoundResouceException> {
            useCase.execute(request, token)
        }
    }

    private fun createValidRequest() =
        UserRequestDTO(
            username = "claus",
            email = "claus@test.com",
            password = "Password123!",
            userRole = UserRole.COMPANY_ADMIN,
            companyId = 1L,
            name = "Claus",
            cpf = "12345678901"
        )

    private fun createUser(
        role: UserRole,
        companyId: Long? = null,
    ) = User(
        id = 1L,
        username = "admin",
        email = "admin@test.com",
        password = "hash",
        role = role,
        company = companyId?.let { Company(id = it, name = "Company $it") }
    )
}