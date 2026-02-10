package br.com.claus.sellvia.application.usecase.category

import br.com.claus.sellvia.application.port.TokenServicePort
import br.com.claus.sellvia.domain.enums.UserRole
import br.com.claus.sellvia.domain.exception.NotFoundResouceException
import br.com.claus.sellvia.domain.exception.WithoutPermissionException
import br.com.claus.sellvia.domain.model.Category
import br.com.claus.sellvia.domain.model.Company
import br.com.claus.sellvia.domain.repository.CategoryRepository
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class DeleteCategoryUseCaseTest {

    private val repository = mockk<CategoryRepository>()
    private val tokenService = mockk<TokenServicePort>()

    private lateinit var useCase: DeleteCategoryUseCase

    @BeforeEach
    fun setup() {
        useCase = DeleteCategoryUseCase(
            categoryRepository = repository,
            tokenService = tokenService
        )
    }

    @Test
    fun `should delete category successfully when user has permission`() {
        val id = 1L
        val companyId = 10L
        val category = Category(id = id, company = Company(id = companyId))

        every { repository.findById(id) } returns category
        every { tokenService.getClaimFromToken("companyId") } returns companyId.toString()
        every { tokenService.getClaimFromToken("role") } returns UserRole.COMPANY_USER.toString()
        every { repository.deleteById(id) } just Runs

        useCase.execute(id)

        verify(exactly = 1) { repository.deleteById(id) }
    }

    @Test
    fun `should throw IllegalArgumentException when id is zero or negative`() {
        val invalidId = 0L

        val exception = assertThrows<IllegalArgumentException> {
            useCase.execute(invalidId)
        }

        assertEquals("ID da categoria deve ser maior que zero", exception.message)
        verify(exactly = 0) { repository.findById(any()) }
    }

    @Test
    fun `should throw NotFoundResouceException when category does not exist`() {
        val id = 1L
        every { repository.findById(id) } returns null

        val exception = assertThrows<NotFoundResouceException> {
            useCase.execute(id)
        }

        assertEquals("Categoria com ID $id não encontrada", exception.message)
        verify(exactly = 0) { repository.deleteById(any()) }
    }

    @Test
    fun `should throw WithoutPermissionException when user belongs to different company`() {
        val id = 1L
        val category = Category(id = id, company = Company(id = 10L))

        every { repository.findById(id) } returns category
        every { tokenService.getClaimFromToken("companyId") } returns "99" // ID diferente
        every { tokenService.getClaimFromToken("role") } returns UserRole.COMPANY_USER.toString()

        assertThrows<WithoutPermissionException> {
            useCase.execute(id)
        }

        verify(exactly = 0) { repository.deleteById(id) }
    }

    @Test
    fun `should allow deletion when user is SYSTEM_ADMIN even from different company`() {
        val id = 1L
        val category = Category(id = id, company = Company(id = 10L))

        every { repository.findById(id) } returns category
        every { tokenService.getClaimFromToken("companyId") } returns "99"
        every { tokenService.getClaimFromToken("role") } returns UserRole.SYSTEM_ADMIN.toString()
        every { repository.deleteById(id) } just Runs

        useCase.execute(id)

        verify(exactly = 1) { repository.deleteById(id) }
    }

    @Test
    fun `should handle category with null company by defaulting to id 0`() {
        val id = 1L
        val category = Category(id = id, company = null)

        every { repository.findById(id) } returns category
        // Se a categoria não tem empresa, e o usuário não é admin e está na empresa 10, deve barrar
        every { tokenService.getClaimFromToken("companyId") } returns "10"
        every { tokenService.getClaimFromToken("role") } returns UserRole.COMPANY_USER.toString()

        assertThrows<WithoutPermissionException> {
            useCase.execute(id)
        }
    }
}