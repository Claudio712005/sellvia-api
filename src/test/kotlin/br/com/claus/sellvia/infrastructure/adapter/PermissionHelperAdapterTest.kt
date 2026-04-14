package br.com.claus.sellvia.infrastructure.adapter

import br.com.claus.sellvia.application.port.TokenServicePort
import br.com.claus.sellvia.domain.enums.UserRole
import br.com.claus.sellvia.domain.exception.InvalidTokenException
import br.com.claus.sellvia.domain.exception.WithoutPermissionException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PermissionHelperAdapterTest {
    private val tokenService = mockk<TokenServicePort>()
    private lateinit var adapter: PermissionHelperAdapter

    @BeforeEach
    fun setup() {
        adapter = PermissionHelperAdapter(tokenService)
    }

    @Test
    fun `should allow action when user belongs to the same company`() {
        every { tokenService.getClaimFromToken("companyId") } returns "10"
        every { tokenService.getClaimFromToken("role") } returns "company_admin"

        adapter.verifyUserCanDoesThisAction(10L)
    }

    @Test
    fun `should allow SYSTEM_ADMIN to act on any company`() {
        every { tokenService.getClaimFromToken("companyId") } returns "10"
        every { tokenService.getClaimFromToken("role") } returns "system_admin"

        adapter.verifyUserCanDoesThisAction(99L)
    }

    @Test
    fun `should throw WithoutPermissionException when non-admin accesses different company`() {
        every { tokenService.getClaimFromToken("companyId") } returns "10"
        every { tokenService.getClaimFromToken("role") } returns "company_user"

        shouldThrow<WithoutPermissionException> {
            adapter.verifyUserCanDoesThisAction(20L)
        }
    }

    @Test
    fun `should throw InvalidTokenException when role claim is missing`() {
        every { tokenService.getClaimFromToken("companyId") } returns "10"
        every { tokenService.getClaimFromToken("role") } returns null

        shouldThrow<InvalidTokenException> {
            adapter.verifyUserCanDoesThisAction(10L)
        }
    }

    @Test
    fun `should return authenticated user details`() {
        every { tokenService.getClaimFromToken("companyId") } returns "5"
        every { tokenService.getClaimFromToken("role") } returns "company_admin"
        every { tokenService.getClaimFromToken("userId") } returns "42"

        val details = adapter.getDetailsOfAuthenticatedUser()

        details.companyId shouldBe 5L
        details.role shouldBe UserRole.COMPANY_ADMIN
        details.userId shouldBe 42L
    }

    @Test
    fun `should throw InvalidTokenException when userId claim is missing`() {
        every { tokenService.getClaimFromToken("companyId") } returns "5"
        every { tokenService.getClaimFromToken("role") } returns "company_admin"
        every { tokenService.getClaimFromToken("userId") } returns null

        shouldThrow<InvalidTokenException> {
            adapter.getDetailsOfAuthenticatedUser()
        }
    }
}