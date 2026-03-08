package br.com.claus.sellvia.application.usecase.category

import br.com.claus.sellvia.application.port.PermissionHelperPort
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
    private val permissionHelperPort = mockk<PermissionHelperPort>()

    private lateinit var useCase: DeleteCategoryUseCase

    @BeforeEach
    fun setup() {
        useCase =
            DeleteCategoryUseCase(
                categoryRepository = repository,
                permissionServiceHelperPort = permissionHelperPort,
            )
    }

    @Test
    fun `should delete category successfully when user has permission`() {
        val id = 1L
        val companyId = 10L
        val category = Category(id = id, company = Company(id = companyId))

        every { repository.findById(id) } returns category
        every { permissionHelperPort.verifyUserCanDoesThisAction(companyId) } just Runs
        every { repository.deleteById(id) } just Runs

        useCase.execute(id)

        verify(exactly = 1) { repository.deleteById(id) }
        verify(exactly = 1) { permissionHelperPort.verifyUserCanDoesThisAction(companyId) }
    }

    @Test
    fun `should throw IllegalArgumentException when id is zero or negative`() {
        val invalidId = 0L

        val exception =
            assertThrows<IllegalArgumentException> {
                useCase.execute(invalidId)
            }

        assertEquals("ID da categoria deve ser maior que zero", exception.message)
        verify(exactly = 0) { repository.findById(any()) }
    }

    @Test
    fun `should throw NotFoundResouceException when category does not exist`() {
        val id = 1L
        every { repository.findById(id) } returns null

        val exception =
            assertThrows<NotFoundResouceException> {
                useCase.execute(id)
            }

        assertEquals("Categoria com ID $id não encontrada", exception.message)
        verify(exactly = 0) { repository.deleteById(any()) }
    }

    @Test
    fun `should throw WithoutPermissionException when permission helper denies action`() {
        val id = 1L
        val companyId = 10L
        val category = Category(id = id, company = Company(id = companyId))

        every { repository.findById(id) } returns category
        every { permissionHelperPort.verifyUserCanDoesThisAction(companyId) } throws WithoutPermissionException("Sem permissão")

        assertThrows<WithoutPermissionException> {
            useCase.execute(id)
        }

        verify(exactly = 0) { repository.deleteById(id) }
    }

    @Test
    fun `should allow deletion when permission helper validates admin access`() {
        val id = 1L
        val companyId = 10L
        val category = Category(id = id, company = Company(id = companyId))

        every { repository.findById(id) } returns category
        every { permissionHelperPort.verifyUserCanDoesThisAction(any()) } just Runs
        every { repository.deleteById(id) } just Runs

        useCase.execute(id)

        verify(exactly = 1) { repository.deleteById(id) }
    }
}